package com.minhhung.controller;

import com.minhhung.connector.HyperledgerAPI;
import com.minhhung.model.Job;
import com.minhhung.service.CheckerService;
import com.minhhung.service.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class HomeController {

    @Autowired
    private HRService hrService;

    @Autowired
    private HyperledgerAPI hyperledgerAPI;

    @GetMapping("/")
    public String home1() {

        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ("anonymousUser".equals(obj)) {
            return "redirect:home";
        } else {
            UserDetails userDetails = (UserDetails) obj;
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
            SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
            SimpleGrantedAuthority ROLE_STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
            SimpleGrantedAuthority ROLE_ISSUER = new SimpleGrantedAuthority("ROLE_ISSUER");
            SimpleGrantedAuthority ROLE_CHECKER = new SimpleGrantedAuthority("ROLE_CHECKER");
            SimpleGrantedAuthority ROLE_HR = new SimpleGrantedAuthority("ROLE_HR");

            if (authorities.contains(ROLE_ADMIN)) {
                return "redirect:admin/admin_home";
            } else if (authorities.contains(ROLE_STUDENT)) {
                return "redirect:student/student_home";
            } else if (authorities.contains(ROLE_CHECKER)) {
                return "redirect:checker/checker_home";
            } else if (authorities.contains(ROLE_ISSUER)) {
                return "redirect:issuer/issuer_home";
            } else if (authorities.contains(ROLE_HR)) {
                return "redirect:hr/hr_home";
            }
        }

        return "redirect:home";
    }

    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/verify")
    public String verify() {
        return "verify";
    }

    @GetMapping("/download")
    public String download() {
        return "download";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }

    @GetMapping("/404")
    public String error404() {
        return "error/404";
    }



    // ----------- Student ---------------
    @GetMapping("/student/student_home")
    public String student() {
        return "student/student_home";
    }

    @GetMapping("/student/apply_form")
    public String applyForm(Model model) {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) obj;
        String username = userDetails.getUsername();
        model.addAttribute("identity", username);
        return "student/apply_form";
    }

    @GetMapping("/student/apply_history")
    public String apply_history() {
        return "student/apply_history";
    }

    @GetMapping("/student/issued_certificate")
    public ModelAndView issued_certificate() {
        ModelAndView mav = new ModelAndView("student/issued_certificate");
        return mav;
    }

    @GetMapping("/student/revoked_certificate")
    public ModelAndView revoked_certificate() {
        ModelAndView mav = new ModelAndView("student/revoked_certificate");
        return mav;
    }

    @GetMapping("/student/jobs_list")
    public ModelAndView jobs_list() {
        ModelAndView mav = new ModelAndView("student/jobs_list");
        return mav;
    }

    @GetMapping("/student/job_single/{job_id}")
    public ModelAndView job_single(@PathVariable String job_id) {
        ModelAndView mav = new ModelAndView("student/job_single");
        Job job = hrService.getJobById(job_id);
        if(job != null) {
            mav.addObject("job", job);
        }
        return mav;
    }


    @GetMapping("/student/apply_job/{job_id}")
    public ModelAndView apply_job(@PathVariable String job_id) {
        ModelAndView mav = new ModelAndView("student/apply_job");
        return mav;
    }


    // ------------ Checker ---------------
    @GetMapping("/checker/checker_home")
    public String checker() {
        return "checker/checker_home";
    }

    @GetMapping("/checker/checker_form") //Show student conf form when click see detail at check home page
    public ModelAndView checker_form() {
        ModelAndView mav = new ModelAndView("checker/checker_form");
        mav.addObject("certsTemplate", hyperledgerAPI.getAllCertificateTemplate());
        return mav;
    }

    @GetMapping("/checker/checked_history_list")
    public ModelAndView checked_history_list() {
        ModelAndView mav = new ModelAndView("checker/checked_history_list");
        return mav;
    }

    @GetMapping("/checker/unchecked_list")
    public ModelAndView unchecked_list() {
        ModelAndView mav = new ModelAndView("checker/unchecked_list");
        return mav;
    }

    @GetMapping("/checker/merged_certificate_list")
    public ModelAndView merged_certificate_list() {
        ModelAndView mav = new ModelAndView("checker/merged_certificate_list");
        return mav;
    }

    @GetMapping("/checker/unmerged_certificate_list")
    public ModelAndView unmerged_certificate_list() {
        ModelAndView mav = new ModelAndView("checker/unmerged_certificate_list");
        return mav;
    }


    // -------------- Issuer -----------
    @GetMapping("/issuer/issuer_home")
    public String issuer() {
        return "issuer/issuer_home";
    }

    @GetMapping("/issuer/unbroadcasted_certificate_list")
    public ModelAndView unbroadcasted_certificate_list() {
        ModelAndView mav = new ModelAndView("issuer/unbroadcasted_certificate_list");
        return mav;
    }

    @GetMapping("/issuer/broadcasted_certificate_list")
    public ModelAndView broadcasted_certificate_list() {
        ModelAndView mav = new ModelAndView("issuer/broadcasted_certificate_list");
        return mav;
    }

    // ----------- HR ---------------
    @GetMapping({"/hr/hr_home", "/hr/hr_jobs"})
    public String hr() {
        return "hr/hr_home";
    }

    @GetMapping("/hr/job_form")
    public String job_form() {
        return "hr/job_form";
    }

    @GetMapping("/hr/applied_job/{job_id}")
    public String applied_job(@PathVariable String job_id) {
        return "hr/applied_job";
    }

    @GetMapping("/hr/applied_job_detail")
    public String applied_job_detail(@RequestParam("id") String appliedId) {
        return "hr/applied_job_detail";
    }


    // ----------- Admin --------------
    @GetMapping("admin/admin_home")
    public String admin_home() {
        return "admin/admin_home";
    }

    @GetMapping("/admin/admin_index")
    public String admin_index() {
        return "admin/admin_index";
    }

    @GetMapping("/admin/broadcasted_transaction_list")
    public ModelAndView broadcasted_transaction_list() {
        ModelAndView mav = new ModelAndView("admin/broadcasted_transaction_list");
        mav.addObject("transactions", hyperledgerAPI.getAllAddRosterTransaction());
        return mav;
    }

    @GetMapping("/admin/certificate_template")
    public ModelAndView certificate_template() {
        ModelAndView mav = new ModelAndView("admin/certificate_template");
        mav.addObject("certsTempl", hyperledgerAPI.getAllCertificateTemplate());
        return mav;
    }
}
