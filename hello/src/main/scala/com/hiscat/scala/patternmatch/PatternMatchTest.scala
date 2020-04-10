package com.hiscat.scala.patternmatch

import scala.util.Random

object PatternMatchTest {
  def main(args: Array[String]): Unit = {
    val x = Random.nextInt(10)
    println(x)
    println(x match {
      case 0 => "zero"
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case _ => "other"
    })
    println(matchTest(2))
    println(matchTest(0))
    println(showNotification(SMS("123", "Are you there?")))
    println(showNotification(VoiceRecording("Tom", "www.baidu.com")))
    println()
    val importantPeopleInfo = Seq("867-5309", "jenny@gmail.com")

    val someSms = SMS("123-4567", "Are you there?")
    val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")
    val importantEmail = Email("jenny@gmail.com", "Drinks tonight?", "I'm free after 5!")
    val importantSms = SMS("867-5309", "I'm here! Where are you?")

    println(showImportantNotification(someSms, importantPeopleInfo)) //prints You got an SMS from 123-4567! Message: Are you there?
    println(showImportantNotification(someVoiceRecording, importantPeopleInfo)) //prints You received a Voice Recording from Tom! Click the link to hear it: voicerecording.org/id/123
    println(showImportantNotification(importantEmail, importantPeopleInfo)) //prints You got an email from special someone!
    println(showImportantNotification(importantSms, importantPeopleInfo))

    println()
    println(goIdle(Phone("huawei")))
    println(goIdle(Computer("mac")))

    println()
    println(findPlaceToSit(Couch()))
    println(findPlaceToSit(Chair()))
  }

  sealed abstract class Furniture

  case class Couch() extends Furniture

  case class Chair() extends Furniture

  def findPlaceToSit(furniture: Furniture): String = furniture match {
    case a: Couch => "Lie on the couch"
    case b: Chair => "Sit on the chair"
  }

  def showImportantNotification(notification: Notification,
                                importantPeopleInfo: Seq[String]): String = {
    notification match {
      case Email(sender, _, _) if importantPeopleInfo.contains(sender) =>
        "You got an email from special someone!"
      case SMS(caller, _) if importantPeopleInfo.contains(caller) =>
        "You got an SMS from special someone!"
      case other => showNotification(other)
    }
  }

  abstract class Device

  case class Phone(model: String) extends Device {
    def screenOff = "Turning screen off"
  }

  case class Computer(model: String) extends Device {
    def screenOff = "Turning screen saver on..."
  }

  def goIdle(device: Device): String = device match {
    case p: Phone => p.screenOff
    case c: Computer => c.screenOff
  }

  def showNotification(notification: Notification): String = {
    notification match {
      case Email(sender, title, _) => s"You got an email from $sender with title: $title"
      case SMS(number, message) => s"You got an SMS from $number! Message: $message"
      case VoiceRecording(name, link) => s"You received a Voice Recording from $name! Click the link to hear it: $link"
    }
  }

  abstract class Notification

  case class Email(sender: String, title: String, body: String) extends Notification

  case class SMS(caller: String, message: String) extends Notification

  case class VoiceRecording(contactName: String, link: String) extends Notification


  def matchTest(x: Int): String = x match {
    case 1 => "one"
    case 2 => "two"
    case _ => "other"
  }

}
