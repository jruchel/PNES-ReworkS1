package org.ekipa.pnes.api.configs.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MySecurityConfig extends WebSecurityConfigurerAdapter {

    protected static List<String> whitelist;

    protected static List<Endpoint> getAllEndpoints() {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Controller c : Controller.getAccessibleControllers()) {
            RequestMapping controllerMapping = Arrays.stream(c.getClass().getDeclaredAnnotationsByType(RequestMapping.class)).findFirst().orElse(null);
            String controllerPath;
            try {
                controllerPath = Arrays.stream(controllerMapping.value()).findFirst().orElse("");
            } catch (Exception ex) {
                controllerPath = "";
            }
            for (Method m : c.getClass().getDeclaredMethods()) {
                SecuredMapping securedMapping = Arrays.stream(m.getDeclaredAnnotationsByType(SecuredMapping.class)).findFirst().orElse(null);
                if (securedMapping != null) {
                    endpoints.add(new Endpoint(controllerPath + securedMapping.path(), securedMapping.role(), securedMapping.method()));
                }
            }
        }
        return endpoints;
    }

    protected HttpSecurity setupEndpoints(HttpSecurity http) throws Exception {
        List<Endpoint> endpointList = getAllEndpoints();
        Set<String> roles = getRoles(endpointList);
        roles.removeIf(r -> r.equals(""));
        for (String role : roles) {
            List<Endpoint> roleEndpoints = getEndpointsWithRole(endpointList, role);
            String[] endpointPaths = stringListToArray(roleEndpoints.stream().map(Endpoint::getPath).collect(Collectors.toList()));
            http = http.authorizeRequests().antMatchers(endpointPaths).hasRole(role.toUpperCase()).and();
        }
        List<Endpoint> permitAllEndpoints = getEndpointsWithRole(endpointList, "");
        List<String> endpointStrings = permitAllEndpoints.stream().map(Endpoint::getPath).collect(Collectors.toList());
        endpointStrings.addAll(whitelist);
        String[] endpointPaths = stringListToArray(endpointStrings);
        if (endpointPaths.length > 0)
            return http.authorizeRequests().antMatchers(endpointPaths).permitAll().and();
        else return http;

    }

    protected String[] stringListToArray(List<String> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    protected List<Endpoint> getEndpointsWithRole(List<Endpoint> endpoints, String role) {
        List<Endpoint> roleEndpoints = new ArrayList<>();
        for (int i = endpoints.size() - 1; i >= 0; i--) {
            Endpoint endpoint = endpoints.get(i);
            if (endpoint.getRole().equals(role)) {
                roleEndpoints.add(endpoint);
            }
        }
        return roleEndpoints;
    }

    protected Set<String> getRoles(List<Endpoint> endpoints) {
        return endpoints.stream().map(Endpoint::getRole).collect(Collectors.toSet());
    }

    protected static void addToWhitelist(String s) {
        whitelist.add(s);
    }

    protected static void clearWhitelist() {
        whitelist = new ArrayList<>();
    }

    protected static void setWhitelist(List<String> whitelist) {
        MySecurityConfig.whitelist = whitelist;
    }

    protected static void addToWhitelist(List<String> s) {
        whitelist.addAll(s);
    }
}
