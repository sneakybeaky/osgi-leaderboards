package com.ninedemons.leaderboard.web.controller;

import com.ninedemons.leaderboard.api.Leaderboard;
import com.ninedemons.leaderboard.api.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jon Barber
 */
@Controller
public class LeaderboardController {

    @Autowired
    Leaderboard leaderboard;


    @RequestMapping(value = "/{leaderboardName}/page/{pageNumber}", method = {RequestMethod.GET})
    public ModelAndView page(@PathVariable String leaderboardName, @PathVariable int pageNumber) {
        Page page = leaderboard.page(leaderboardName, pageNumber);
        ModelAndView mav = new ModelAndView("page");
        mav.addObject("page",page);
        mav.addObject("leaderboardName",leaderboardName);
        mav.addObject("pageNumber",pageNumber);
        return mav;
    }

}
