package com.recruitment.controller;

import com.recruitment.domain.Candidate;
import com.recruitment.dto.CandidateDTO;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.dto.SearchDTO;
import com.recruitment.service.CandidateService;
import com.recruitment.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

    @Autowired
    ExperienceService experienceService;

    @RequestMapping("")
    public ModelAndView candidates(){
        ModelAndView modelAndView = new ModelAndView("candidatesList");
        List<CandidateDTO> candidates = candidateService.getAllCandidates();
        modelAndView.addObject("candidates", candidates);
        return modelAndView;
    }

    @RequestMapping("/form")
    public String candidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidateForm";
    }

    @PostMapping
    public String createCandidate(@ModelAttribute CandidateDTO candidateDTO) {
        candidateService.createCandidate(candidateDTO);
        return "redirect:/candidates";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }

    @GetMapping("/{id}")
    public String getCandidateById(@PathVariable Long id, Model model) {
        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        ExperienceDTO experienceDTO = experienceService.getByCandidateId(id);
        model.addAttribute("candidate", candidateDTO);
        model.addAttribute("experience", experienceDTO);
        return "candidateDetails";
    }

    @PutMapping("/{id}")
    public CandidateDTO updateCandidate(@PathVariable Long id, @RequestBody CandidateDTO candidateDTO) {
        candidateDTO.setId(id);
        return candidateService.updateCandidate(candidateDTO);
    }

    @GetMapping("/filter")
    public List<CandidateDTO> getFilteredCandidates(@RequestBody SearchDTO searchDTO) {
        return candidateService.getFilteredCandidates(searchDTO);
    }
}
