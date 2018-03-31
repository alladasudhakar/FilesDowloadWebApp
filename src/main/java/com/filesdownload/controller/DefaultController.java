package com.filesdownload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.filesdownload.util.GoogleDriveUtil;

@Controller
public class DefaultController {

	@RequestMapping("/")
	public String index() {
		return "login";
	}
	
	@RequestMapping("/authcode")
	public String authcode() {
		return "authcode";
	}

	@PostMapping("/googledrive")
	public String sayHello(@RequestParam("name") String name, Model model) {
		//System.out.println("drive = "+GoogleDriveUtil.getDrive());
		model.addAttribute("name", name);
		return "googledrive";
	}
}
