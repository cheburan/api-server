package uk.ac.ncl.openlab.intake24.api.shared

case class Credentials(survey_id: String, username: String, password: String)

case class SigninResult(refreshToken: String)

case class RefreshResult(refreshToken: String, accessToken: String)