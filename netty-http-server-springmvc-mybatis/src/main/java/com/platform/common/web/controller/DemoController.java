package com.platform.common.web.controller;

import com.platform.common.web.BaseSpringController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/demo")
public class DemoController extends BaseSpringController {

	private final String LIST_ACTION = "redirect:/demo/list";

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

	/**
	 * List view.
	 */
	@RequestMapping("/list")
	public void list() {
		this.put("currentUserName", "Andy.Zheng");
		this.put("dataList", demoVoList);
	}

	/**
	 * Show view.
	 */
	@RequestMapping("/show")
	public void show(final Long id, final ModelMap model) throws Exception {
		model.put("demo", demoVoList.get(id.intValue()));
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
	public void edit(final Long id, final ModelMap model) throws Exception {
		model.put("demo", demoVoList.get(id.intValue()));
	}

	/**
	 * Save View.
	 */
	@RequestMapping("/save")
	public String save(Long id, final DemoVo demoVo) throws Exception {
		int index;
		if (null == id) { // create
			index = demoVoList.size();
			demoVo.setId(index);
		} else { // update
			index = id.intValue();
		}
		demoVoList.add(index, demoVo);

		return LIST_ACTION;
	}

	/**
	 * Delete View.
	 */
	@RequestMapping("/delete")
	public String delete(final String[] ids) {
		for (int i = 0 ; i < ids.length; i++) {
			demoVoList.remove(i);
		}
		return LIST_ACTION;
	}

	/**
	 * Response to json for the ajax request.
	 * Note: The request header must be container <code>"Accept": "application/json"</code>
	 */
	@RequestMapping("/toJson")
	@ResponseBody
	public DemoVo toJson(final Long id) throws Exception {
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


