package me.hhac.android.greetings.models

class BaseApiResponse(
    var data: Any?,
    var status: Int,
    var message: String?,
    var errors: List<FieldErrorDto>,
    var time: String?
) {


}