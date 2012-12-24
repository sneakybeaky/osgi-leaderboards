package com.ninedemons.leaderboard.web.controller;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jon Barber
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {

    @Autowired
    BundleContext bundleContext;

    @Autowired
    ConfigurationAdmin configurationAdmin;

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView home() throws Exception {

        ModelAndView mav = new ModelAndView("home");

        Bundle[] bundles = bundleContext.getBundles();
        mav.addObject("bundles", bundles);

        Configuration[] configurations = configurationAdmin.listConfigurations(null);
        mav.addObject("configurations", configurations);
        return mav;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public ConfigurationAdmin getConfigurationAdmin() {
        return configurationAdmin;
    }

    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }
}