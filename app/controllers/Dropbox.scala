package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.Configuration

import com.dropbox.core.{DbxAppInfo, DbxAuthFinish, DbxEntry, DbxWebAuthNoRedirect}
import com.dropbox.core.DbxEntry.WithChildren

import scalaj.http.Http

import dropbox4s.core.CoreApi
import dropbox4s.core.model.DropboxPath

import scala.language.postfixOps
import scala.collection.JavaConverters._

object Dropbox extends Controller with CoreApi {

  val app: Application = Play.unsafeApplication
  val conf: Configuration = app.configuration

  // Get values from the config file. Don't handle the Option
  // as we want the application to crash if they are not
  // provided
  val applicationName = conf.getString("dropbox.app.name").get
  val version = conf.getString("dropbox.app.version").get
  val redirectUri = conf.getString("dropbox.app.redirectUri").get
  val appKey = conf.getString("dropbox.app.key").get
  val appSecret = conf.getString("dropbox.app.secret").get

  val appInfo = new DbxAppInfo(appKey, appSecret);
  val webAuth = new DbxWebAuthNoRedirect(requestConfig, appInfo);

  def index = Action { request =>
    val csrf = models.Dropbox.generateCsrf

    // Store the csrf value in the session
    Ok(views.html.dropbox.index(appKey, redirectUri, csrf)).withSession(request.session + ("csrf" -> csrf))
  }

  def authFinish(code: String, state: String) = Action { request =>
    // Check if the csrf sent by the callback is the same than the one
    // we previously stored in the session
    request.session.get("csrf").map { csrf =>

      if(csrf == state) {
        val response = Http("https://api.dropbox.com/1/oauth2/token")
          .postForm(Seq("code" -> code, "grant_type" -> "authorization_code", "redirect_uri" -> redirectUri))
          .auth(appKey, appSecret)
          .asString

        if(response.code == 200) {
          val json: JsValue = Json.parse(response.body)
          val access_token: String = (json \ "access_token").as[String]

          // Storing the access_token in the session, it's certainly not a good idea
          // but hey, it's a test case ;)
          Ok(views.html.dropbox.authFinish()).withSession(request.session + ("access_token" -> access_token))
        } else {
          InternalServerError("Error when finishing the oAuth process: " + response.body)
        }

      } else
        Unauthorized("Csrf values doesn't match.")
    }.getOrElse {
      Unauthorized("Bad csrf value.")
    }
  }

  def listDirectory() = Action { request =>

    request.session.get("access_token").map { accessToken =>

      // Dirty hack, but dropbox4s requires the full DbxAuthFinish
      // object, even if it's only using the token
      implicit val auth: DbxAuthFinish = new DbxAuthFinish(accessToken, "", "")
      val appPath = DropboxPath("/")
      val children: List[DbxEntry] = (appPath children).children.asScala.toList

      // List directory
      //val children: WithChildren = client(accessToken).getMetadataWithChildren("/")
      Ok(views.html.dropbox.listDirectory(children))
    }.getOrElse {
      Unauthorized("No access_token available.")
    }

  }

}

