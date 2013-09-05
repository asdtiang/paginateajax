
<%@ page import="org.asdtiang.grails.paginate.TestAjax" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <jq:resources />
        <paginate:resources/>
        <g:set var="entityName" value="${message(code: 'testAjax.label', default: 'TestAjax')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        
    </head>
    <body>
    <div id="update">
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'testAjax.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'testAjax.name.label', default: 'Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${dataList}" status="i" var="testAjaxInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${testAjaxInstance.id}">${fieldValue(bean: testAjaxInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: testAjaxInstance, field: "name")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <paginate:jquery update="update" sample="true" total="${dataTotal}" selectMax="15" />
                <paginate:jquery update="update"  total="${dataTotal}" selectMax="15" />
            </div>
        </div>
        </div>
    </body>
</html>
