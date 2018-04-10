package com.filesdownload.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DefaultController {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "E://temp//";
	
	public static String fileName = "";

	@RequestMapping("/")
	public String index() {
		return "login";
	}

	@RequestMapping("/authcode")
	public String authcode() {
		return "authcode";
	}

	@RequestMapping(value = "/uploadfile", method = RequestMethod.GET)
	public String uploadfile() {
		return "uploadfile";
	}

	@GetMapping("/googledrive")
	public String googleDrive() {
		return "googledrive";
	}
	/*
	@PostMapping("/googledrive")
	public String sayHello(@RequestParam("name") String name, Model model) {
		model.addAttribute("name", name);
		return "googledrive";
	}
	*/
	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	public String submit(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadfile";
		}

		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			fileName = file.getOriginalFilename();
			request.getSession().setAttribute("fileName", "" + file.getOriginalFilename());
			request.getSession().setAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		//return "redirect:/fileUploadView";
		return "redirect:/googledrive";
	}
	
	@RequestMapping(value = "/fileUploadView", method = RequestMethod.GET)
	public String fileUploadView(RedirectAttributes redirectAttributes, HttpServletRequest request) {
		System.out.println("flash attributes :: "+request.getSession().getAttribute("message"));
		return "fileUploadView";
	}
}
