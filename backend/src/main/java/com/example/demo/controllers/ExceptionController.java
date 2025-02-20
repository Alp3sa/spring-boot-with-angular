package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.boot.web.servlet.error.ErrorController;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController implements ErrorController {

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {
      return "redirect:/";
  }

  @Override
  public String getErrorPath() {
      return "/error";
  }
}