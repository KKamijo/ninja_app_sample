/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import ninja.Result;
import ninja.Results;
import ninja.Context;
import ninja.FilterWith;
import ninja.jaxy.GET;
import ninja.jaxy.POST;
import ninja.jaxy.Path;
import ninja.params.Param;
import ninja.validation.IsInteger;
import ninja.validation.Required;
import ninja.validation.Validation;
import ninja.session.FlashScope;

import filters.FirstFilter;
import filters.SecondFilter;

import com.google.inject.Singleton;

import org.seasar.doma.jdbc.tx.TransactionManager;

import doma.entity.Employee;
import doma.dao.EmployeeDao;
import doma.dao.AppDao;
import doma.transaction.DomaTransactional;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;



/**
 * Application Controller
 * Defining Application Functions & Routes
 *
 */
@Singleton
@Path("")
public class ApplicationController {
    Map<String, Object> map = new HashMap<>();

    @Path("/")
    @GET
    public Result index() {
        return Results.html();

    }

    @Path("/test")
    @GET
    @DomaTransactional
    public Result test() {
        map = new HashMap<>();
        map.put("m1", "Honour thy error as a hidden intention.");
        map.put("m2", "You don't have to be ashamed of using your own ideas");
        map.put("m3", "Use an unacceptable color");
        map.put("name", "");
        map.put("age", "");
        
        List<String> tests = new ArrayList<String>();
        getEmployees(tests);
        map.put("tests", tests);
        return Results.html().render(map).template("views/ApplicationController/test.ftl.html");
    }
    
    @Path("/hello_world.json")
    @GET
    public Result helloWorldJson() {
        
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";

        return Results.json().render(simplePojo);

    }


    @Inject
    private EmployeeDao dao;

    @Path("/add")
    @POST
    @FilterWith({
        FirstFilter.class,
        SecondFilter.class})
    @DomaTransactional
    public Result add(
        @Param("name") String name, 
        @Param("age") @Required @IsInteger String age, 
        Validation validation,
        Context context) {

        if (validation.hasViolations()) {
            map.put("name", name);
            map.put("age", age);
        }
        else {
            String foo = (String)context.getAttribute("foo");
            Employee employee = new Employee();
            employee.name = name;
            employee.age = Integer.parseInt(age);
            dao.insert(employee);
            map.put("name", "");
            map.put("age", "");
        }

        List<String> tests = new ArrayList<String>();
        getEmployees(tests);
        map.put("errors", validation.getFieldViolations());
        map.put("tests", tests);

        return Results.html().render(map).template("views/ApplicationController/test.ftl.html");
    }
    
    public static class SimplePojo {

        public String content;
        
    }

    private void getEmployees(List<String> tests) {
        List<Employee> employees = dao.selectAll();
        for (Employee employee : employees) {
            tests.add(employee.name);
        }
    }
}
