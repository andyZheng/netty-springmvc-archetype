package com.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("prototype")
@RequestMapping("/test")
public class TestController {

    private static List<DemoVo> demoVoList = new ArrayList<DemoVo>();

    static {
        for (int i = 0; i < 20; i++) {
            DemoVo demoVo = new DemoVo();
            demoVo.setId(i);
            demoVo.setName("Name" + i);
            demoVo.setRemarks("Remarks" + i);

            demoVoList.add(demoVo);
        }
    }

    @RequestMapping("/simple")
    public @ResponseBody String simple() {
        return "simple";
    }

    @RequestMapping("/asyncSimple")
    public @ResponseBody Callable<String> asyncSimple() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "asyncSimple";
            }
        };
    }

    @RequestMapping("/asyncRuntimeException")
    public Callable<String> asyncRuntimeException() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw new RuntimeException("On purpose");
            }
        };
    }

    @RequestMapping("/asyncException")
    public Callable<String> asyncException() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw new Exception("On purpose");
            }
        };
    }

    @RequestMapping("/runtimeException")
    public String runtimeException() {
        throw new RuntimeException("On purpose");
    }

    @RequestMapping("/exception")
    public String exception() throws Exception {
        throw new Exception("On purpose");
    }

    @RequestMapping("/error")
    public void error(HttpServletResponse response) {
        try {
            response.sendError(501, "On purpose: Not Implemented");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/printWriter")
    public void printWriter(HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.append("Using print writer response");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List view.
     */
    @RequestMapping("/list")
    @ResponseBody
    public ModelMap list(final ModelMap model) {
        model.put("currentUserName", "Andy.Zheng");
        model.put("dataList", demoVoList);
        
        return model;
    }

    /**
     * Show view.
     */
    @RequestMapping("/show")
    @ResponseBody
    public ModelMap show(final Long id, final ModelMap model) throws Exception {
        model.put("demo", demoVoList.get(id.intValue()));
        
        return model;
    }

    /**
     * Go to create view.
     */
    @RequestMapping("/create")
    public void create() throws Exception {
        // DO NOTHING
    }

    /**
     * Go to edit view..
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ModelMap edit(final Long id, final ModelMap model) throws Exception {
        model.put("demo", demoVoList.get(id.intValue()));
        
        return model;
    }

    /**
     * Save View.
     */
    @RequestMapping("/save")
    @ResponseBody
    public DemoVo save(Long id, final DemoVo demoVo) throws Exception {
        int index;
        if (null == id) { // create
            index = demoVoList.size();
            demoVo.setId(index);
        } else { // update
            index = id.intValue();
        }
        demoVoList.add(index, demoVo);

        return demoVo;
    }

    /**
     * Delete View.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(final String[] ids) {
        for (int i = 0 ; i < ids.length; i++) {
            demoVoList.remove(i);
        }
        return "OK";
    }

    /**
     * Response to json for the ajax request.
     * Note: The request header must be container <code>"Accept": "application/json"</code>
     */
    @RequestMapping("/toJSON")
    @ResponseBody
    public DemoVo toJSON(final Long id) throws Exception {
        return demoVoList.get(id.intValue());
    }

    /**
     * Response to XML for the ajax request.</p>
     * Note: The request header must be container <code>"Accept": "application/xml"</code>
     */
    @RequestMapping("/toXML")
    @ResponseBody
    public DemoVo toXML(final Long id) throws Exception {
        return demoVoList.get(id.intValue());
    }

    /**
     * Response to Json/XML for the ajax request.</p>
     */
    @RequestMapping({"/format/{id}.json", "/format/{id}.xml"})
    @ResponseBody
    public DemoVo format(@PathVariable Long id) throws Exception {
        return demoVoList.get(id.intValue());
    }



    @XmlRootElement
    public static class DemoVo implements Serializable {
        private int id;
        private String name;
        private String remarks;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
