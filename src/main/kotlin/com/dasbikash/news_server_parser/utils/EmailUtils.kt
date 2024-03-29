/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasbikash.news_server_parser.utils

import com.dasbikash.news_server_data_coordinator.model.EmailAuth
import com.dasbikash.news_server_data_coordinator.model.EmailTargets
import com.dasbikash.news_server_parser.model.AuthToken
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.activation.FileDataSource

object EmailUtils {

    private const val EMAIL_AUTH_FILE_LOCATION = "/email_details_auth.json"
    private const val EMAIL_TARGET_DETAILS_FILE_LOCATION = "/email_details_targets.json"
    private const val HTML_CONTENT_TYPE = "text/html; charset=utf-8"
    private const val PLAIN_TEXT_CONTENT_TYPE = "text/plain; charset=utf-8"

    private val emailAuth:EmailAuth
    private val emailTargets:EmailTargets

    private lateinit var properties: Properties
    private lateinit var session: Session

    init {
        emailAuth = FileReaderUtils.jsonFileToEntityList(EMAIL_AUTH_FILE_LOCATION,EmailAuth::class.java)
        emailTargets = FileReaderUtils.jsonFileToEntityList(EMAIL_TARGET_DETAILS_FILE_LOCATION,EmailTargets::class.java)
    }

    fun  emailAuthTokenToAdmin(authToken: AuthToken){
        sendEmail("New Token for Parser App.","Token:\t${authToken.token}\nExpires on:\t${authToken.expiresOn}")
    }

    private fun getProperties():Properties{
        if (!::properties.isInitialized){
            properties = Properties()

            emailAuth.properties!!.keys.asSequence().forEach {
                properties.put(it, emailAuth.properties!!.get(it)!!)
            }
        }
        return properties
    }
    private fun getSession():Session{
        if (!::session.isInitialized){
            session = Session.getInstance(getProperties(),
                    object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(emailAuth.userName, emailAuth.passWord)
                        }
                    })
        }
        return session
    }

    fun sendEmail(subject:String, body:String, filePath:String?=null):Boolean{

        try {
            val message = MimeMessage(getSession())

            message.setFrom(InternetAddress(emailAuth.userName))
            setEmailRecipients(message)
            message.subject = subject

            val messageBodyTextPart: MimeBodyPart = MimeBodyPart()

            messageBodyTextPart.setContent(body.replace("\n","<br>"),HTML_CONTENT_TYPE)

            val multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyTextPart)

            if (filePath!=null && File(filePath).exists()){
                val messageBodyAttachmentPart: MimeBodyPart = MimeBodyPart()
                val source = FileDataSource(filePath)
                messageBodyAttachmentPart.setDataHandler(DataHandler(source))
                messageBodyAttachmentPart.setFileName(filePath.split(Regex("/")).last())
                multipart.addBodyPart(messageBodyAttachmentPart)
            }

            message.setContent(multipart)

            Transport.send(message)
            return true
        } catch (e: MessagingException) {
            e.printStackTrace()
            return false
        }
    }

    private fun setEmailRecipients(message: MimeMessage) {
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(getToAddressString())
        )

        getCcAddressString()?.let {
            message.setRecipients(
                    Message.RecipientType.CC,
                    InternetAddress.parse(it)
            )
        }

        getBccAddressString()?.let {
            message.setRecipients(
                    Message.RecipientType.BCC,
                    InternetAddress.parse(it)
            )
        }
    }

    private fun getToAddressString():String{
        return emailTargets.toAddresses!!
    }

    private fun getCcAddressString():String?{
        return emailTargets.ccAddresses
    }

    private fun getBccAddressString():String?{
        return emailTargets.bccAddresses
    }
}