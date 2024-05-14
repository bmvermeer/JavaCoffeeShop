package nl.brianvermeer.workshop.coffee.controller

import nl.brianvermeer.workshop.coffee.service.PersonService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal

@Controller
class UploadController ( private val personService: PersonService) {

    @GetMapping("/uploadimage")
    fun displayUploadForm(): String {
        return "person/upload"
    }

    @PostMapping("/uploadimage")
    @Throws(IOException::class)
    fun uploadImage(model: Model, @RequestParam("image") file: MultipartFile, principal: Principal): String {
        val name = file.originalFilename.replace(" ", "_")
        val fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, name)
        Files.write(fileNameAndPath, file.bytes)
        model.addAttribute("msg", "Uploaded images: $name")

        val user = principal.name
        val person = personService.findByUsername(user)

        person.profilePic = name
        personService.savePerson(person)
        return "person/upload"
    }

    companion object {
        var UPLOAD_DIRECTORY: String = System.getProperty("user.dir") + "/uploads"
    }
}