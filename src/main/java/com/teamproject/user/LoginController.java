package com.teamproject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/")
public class LoginController
{
    @Autowired
    UserService service;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcome() { return "index"; }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() { return "login"; }

    @RequestMapping(value = "/login_check", method = RequestMethod.POST)
    public String loginCheck(HttpSession session, RedirectAttributes redirectAttr, UserVO vo)
    {
        if (session.getAttribute("login") != null)
        {
            session.removeAttribute("login");
        }

        UserVO loginVO = service.getUser(vo);
        String returnURL = "";

        if (loginVO != null)
        {
            session.setAttribute("login", loginVO);
            returnURL = "redirect:/";
        }
        else
        {
            redirectAttr.addFlashAttribute("message", "wrong");
            returnURL = "redirect:/login";
        }

        return returnURL;
    }

    @RequestMapping(value = "/register_check", method = RequestMethod.POST)
    public String registerCheck(HttpSession session, RedirectAttributes redirectAttr, UserVO vo)
    {
        if (service.insertUser(vo) == 0)
        {
            redirectAttr.addFlashAttribute("message", "no");
            return "redirect:/register";
        }
        else
        {
            redirectAttr.addFlashAttribute("message", "yes");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register() { return "register"; }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttr)
    {
        session.invalidate();
        redirectAttr.addFlashAttribute("message", "bye");
        return "redirect:/login";
    }
}
