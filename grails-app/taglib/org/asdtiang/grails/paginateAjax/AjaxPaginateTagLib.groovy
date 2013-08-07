package org.asdtiang.grails.paginateAjax

import grails.converters.JSON

import org.springframework.web.servlet.support.RequestContextUtils as RCU
class AjaxPaginateTagLib {
	static namespace = "paginate"
	
	def resources={attrs ->
		if(attrs.library=="prototype"){
			out << '<script type="text/javascript" src="'
			out << g.resource(dir:"${pluginContextPath}/js/" , file:"ajaxPage.js")
			out << '"></script>\n'
			}
		else{
			out << '<script type="text/javascript" src="'
			out << g.resource(dir:"${pluginContextPath}/js/" , file:"ajaxPageJquery.js")
			out << '"></script>\n'
			}
		out << """        <link rel="stylesheet" href="${g.resource(dir:"${pluginContextPath}/css/" , file:"ajaxPage.css")}" type="text/css"  />\n"""
		}
	def prototype={attrs ->
		if('false'==params.showMenu){
		  return
		}
		def writer = out
		//获取其它的参数
		def params1=new HashMap()
		params1.putAll(params)
		params1.remove("max")
		params1.remove("offset")
		params1.remove("controller")
		params1.remove("action")
		def jsonString=params1 as JSON
		println "jsonData:"+jsonString.toString()
		def otherParamsUrl=""
		params1.each{
			otherParamsUrl=otherParamsUrl+it.toString()+"&"

			}
	   // println params
		if(!params.offset){
			params.offset=0
	   }
	   else{
			params.offset=params.offset as int
	   }
		params.sort=params.sort==null?"dateCreated":params.sort
		params.order=params.order==null?"desc":params.order
		def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
		def locale = RCU.getLocale(request)
		def total = attrs.total.toInteger()
		def offset = params.offset?.toInteger()
		def max = params.max?.toInteger()
		int pageSize=Math.round(Math.ceil(total / max))
		def path=request.getContextPath()
		def pageNow=offset/max+1
		def selectMax=attrs.selectMax?.toInteger()
		if(!selectMax){
			selectMax=20
		}
		selectMax=selectMax>total?total:selectMax
		////js输出
		writer<<"""
		 <div id="ajaxPage">
		"""
		/////显示首页和上页
		def firstText=messageSource.getMessage('paginate.first', null, messageSource.getMessage('default.paginate.first', null, 'First', locale), locale)
		def prevText=messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Prev', locale), locale)
		def nextText=messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale)
		def lastText=messageSource.getMessage('paginate.last', null, messageSource.getMessage('default.paginate.last', null, 'Last', locale), locale)

		if(offset!=0){
			def firstUrl="""${otherParamsUrl}offset=0&max=${params.max}"""
			writer<<"""
			<span id="firstShow" title="${firstText}" onclick="new Ajax.Updater('${attrs.update}',
		'${path}/${params.controller}/${params.action}',{asynchronous:true,evalScripts:true,parameters:'${firstUrl}'});
		return false;" >
			[${firstText}]
			</span>
			"""
			int prev=offset-max
			def prevUrl="""${otherParamsUrl}offset=${prev}&max=${params.max}"""
			writer<<"""
				<span id="preShow" title="${prevText}" onclick="new Ajax.Updater('${attrs.update}',
			'${path}/${params.controller}/${params.action}',{asynchronous:true,evalScripts:true,parameters:'${prevUrl}'});
			return false;" >
				[${prevText}]
				</span>
				"""
		}
		else{
			writer<<"""<span id="firstNoShow" title="${firstText}">
					  [${firstText}]
					  </span>
				"""
			writer<<"""<span id="preNoShow" title="${prevText}">
					[${prevText}]
					</span>
					"""
		}
		////数据信息输出
		writer<<"""<span id="ajaxPageInfo">当前${pageNow}/${pageSize}页（共${total}）</span>"""

		//下一页和末页输出，
		if(offset!=max*(pageSize-1)){
			int next1=offset+max
			def nextUrl="""${otherParamsUrl}offset=${next1}&max=${params.max}"""

			writer<<"""
				<span id="nextShow"   title="${nextText}" onclick="new Ajax.Updater('${attrs.update}',
			'${path}/${params.controller}/${params.action}',{asynchronous:true,evalScripts:true,parameters:'${nextUrl}'});
			return false;" >
				[${nextText}]
				</span>
				"""
			int last=max*(pageSize-1)
			def lastUrl="""${otherParamsUrl}offset=${last}&max=${params.max}"""
			writer<<"""
				<span id="lastShow" title="${lastText}" onclick="new Ajax.Updater('${attrs.update}',
			'${path}/${params.controller}/${params.action}',{asynchronous:true,evalScripts:true,parameters:'${lastUrl}'});
			return false;" >
				[${lastText}]
				</span>
				"""
		}
		else{
			writer<<"""<span id="nextNoShow" title="${nextText}">
				[${nextText}]
				</span>
				"""
			writer<<"""<span id="lastNoShow" title="${lastText}">
				[${lastText}]
				</span>
				"""
		}
		///到指定页连接输出
		def selectUrl="&${otherParamsUrl}max=${params.max}"
		writer<<"""
		<span id="ajaxSelectText">转到<select id="ajaxSelect" onchange="myUpdate('${attrs.update}','${path}/${params.controller}/${params.action}'
		,'${selectUrl}','${max}')">

		"""
		for(int j=1;j<=pageSize;j++){
			if(pageNow!=j){
				writer<<"""  <option value ="${j}">${j}/${pageSize}</option>           """
			}
			else{
				writer<<"""  <option value ="${j}"  selected="selected">${j}/${pageSize}</option>           """
			}
		}
		writer<<"</select>页</span>"

		////每页显示几条数据
		def maxUrl="&${otherParamsUrl}offset=0"
		writer<<"""
			 <span id="ajaxMaxText"> 每页显示</span><select id="ajaxMax" onchange="myUpdate2('${attrs.update}','${path}/${params.controller}/${params.action}'
			  ,'${maxUrl}',this)">
			  """
		 if(attrs.selectStep){
		   def step=attrs.selectStep as int
		   for(int i=step;i<=selectMax;i=i+step){
				   writer<<"""<option value ="${i}">${i}</option>"""
		  }

		}
		else{
		  for(int j=1;j<=selectMax;j++){

			  if(max!=j){
				  writer<<"""<option value ="${j}">${j}</option>"""
			  }
			  else{
				  writer<<"""<option value ="${j}"  selected="selected">${j}</option>"""
			  }
		  }
		}
		writer<<"</select> </div>"
	}
	
	def jquery={attrs ->
		if('false'==params.showMenu){
		  return
		}
		def writer = out
		//获取其它的参数
		def paramsMap=new HashMap()
		paramsMap.putAll(params)
		paramsMap.remove("max")
		paramsMap.remove("offset")
		paramsMap.remove("controller")
		paramsMap.remove("action")
	   // println params
		if(!params.offset){
			params.offset=0
	   }
	   else{
			params.offset=params.offset as int
	   }
		params.sort=params.sort==null?"dateCreated":params.sort
		params.order=params.order==null?"desc":params.order
		def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
		def locale = RCU.getLocale(request)
		def total = attrs.total.toInteger()
		def offset = params.offset?.toInteger()
		def max = params.max?.toInteger()
		int pageSize=Math.round(Math.ceil(total / max))
		def path=request.getContextPath()
		def pageNow=offset/max+1
		def selectMax=attrs.selectMax?.toInteger()
		if(!selectMax){
			selectMax=20
		}
		selectMax=selectMax>total?total:selectMax
		////js输出
		writer<<"""
		 <div id="ajaxPage">
		"""
		/////显示首页和上页
		def firstText=messageSource.getMessage('paginate.first', null, messageSource.getMessage('default.paginate.first', null, 'First', locale), locale)
		def prevText=messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Prev', locale), locale)
		def nextText=messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale)
		def lastText=messageSource.getMessage('paginate.last', null, messageSource.getMessage('default.paginate.last', null, 'Last', locale), locale)
        def url= "${path}/${params.controller}/${params.action}"
		paramsMap.put("max", params.max)
		if(offset!=0){
			paramsMap.put("offset", 0)
			def firstJson=(paramsMap as JSON).toString().replaceAll('"',"'")
			writer<<"""
			<span id="firstShow" title="${firstText}" onclick="
			
			jQuery.ajax({type:'POST',data:${firstJson},
				url:'${url}',success:function(data,textStatus){
				jQuery('#${attrs.update}').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}}
	         );return false;
	         
	         ">
			[${firstText}]
			</span>
			"""
			paramsMap.put("offset", offset-max)
			def prevJson=(paramsMap as JSON).toString().replaceAll('"',"'")
			writer<<"""
				<span id="preShow" title="${prevText}" onclick="
				jQuery.ajax({type:'POST',data:${prevJson},
				url:'${url}',success:function(data,textStatus){
				jQuery('#${attrs.update}').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}}
	            );return false;
				" >
				[${prevText}]
				</span>
				"""
		}
		else{
			writer<<"""<span id="firstNoShow" title="${firstText}">
					  [${firstText}]
					  </span>
				"""
			writer<<"""<span id="preNoShow" title="${prevText}">
					[${prevText}]
					</span>
					"""
		}
		////数据信息输出
		writer<<"""<span id="ajaxPageInfo">当前${pageNow}/${pageSize}页（共${total}）</span>"""

		//下一页和末页输出，
		if(offset!=max*(pageSize-1)){
			paramsMap.put("offset", offset+max)
            def nextJson=(paramsMap as JSON).toString().replaceAll('"',"'")
			println nextJson
			println nextJson.class
			println url
			def str="""
				<span id="nextShow"   title="${nextText}" onclick="
				jQuery.ajax({type:'POST',data:${nextJson},
				url:'${url}',success:function(data,textStatus){
				jQuery('#${attrs.update}').html(data);},error:function(XMLHttpRequest,textStatus,errorThrown){}}
	            );return false;
				" >
				[${nextText}]
				</span>
				"""
			println str
			writer<<str
			paramsMap.put("offset",max*(pageSize-1))
			def lastJson=(paramsMap as JSON).toString().replaceAll('"',"'")
			writer<<"""
				<span id="lastShow" title="${lastText}" onclick="
				jQuery.ajax({type:'POST',data:${lastJson},
				url:'${url}',success:function(data,textStatus){
				jQuery('#${attrs.update}').html(data);},
				error:function(XMLHttpRequest,textStatus,errorThrown){}}
	            );return false;
				" >
				[${lastText}]
				</span>
				"""
		}
		else{
			writer<<"""<span id="nextNoShow" title="${nextText}">
				[${nextText}]
				</span>
				"""
			writer<<"""<span id="lastNoShow" title="${lastText}">
				[${lastText}]
				</span>
				"""
		}
		///到指定页连接输出
		paramsMap.remove("offset")
		
		def selectJson=(paramsMap as JSON).toString().replaceAll('"',"'")
		writer<<"""
		<span id="ajaxSelectText">转到<select id="ajaxSelect" onchange="
		jqueryUpdate('#${attrs.update}','${path}/${params.controller}/${params.action}'
		,${selectJson},this)">

		"""
		for(int j=1;j<=pageSize;j++){
			if(pageNow!=j){
				writer<<"""  <option value ="${j}">${j}/${pageSize}</option>           """
			}
			else{
				writer<<"""  <option value ="${j}"  selected="selected">${j}/${pageSize}</option>           """
			}
		}
		writer<<"</select>页</span>"

		////每页显示几条数据
		paramsMap.put("offset",0)
		def maxJson=(paramsMap as JSON).toString().replaceAll('"',"'")
		writer<<"""
			 <span id="ajaxMaxText"> 每页显示</span><select id="ajaxMax" onchange="
			 jqueryUpdate1('#${attrs.update}','${path}/${params.controller}/${params.action}'
			  ,${maxJson},this)">
			  """
		 if(attrs.selectStep){
		   def step=attrs.selectStep as int
		   for(int i=step;i<=selectMax;i=i+step){
				   writer<<"""<option value ="${i}">${i}</option>"""
		  }

		}
		else{
		  for(int j=1;j<=selectMax;j++){

			  if(max!=j){
				  writer<<"""<option value ="${j}">${j}</option>"""
			  }
			  else{
				  writer<<"""<option value ="${j}"  selected="selected">${j}</option>"""
			  }
		  }
		}
		writer<<"</select> </div>"
	}
}
