package org.asdtiang.grails.paginate

class TestAjaxController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def ajaxPaginateService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		def hql="""from TestAjax"""
		params.max = Math.min(params.max ? params.max as int : 5, 100)
		params.offset=params.offset?params.offset as int:0	
		ajaxPaginateService.getResultMap(params,TestAjax.class,hql)
    }

    def create = {
        def testAjaxInstance = new TestAjax()
        testAjaxInstance.properties = params
        return [testAjaxInstance: testAjaxInstance]
    }

    def save = {
        def testAjaxInstance = new TestAjax(params)
        if (testAjaxInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), testAjaxInstance.id])}"
            redirect(action: "show", id: testAjaxInstance.id)
        }
        else {
            render(view: "create", model: [testAjaxInstance: testAjaxInstance])
        }
    }

    def show = {
        def testAjaxInstance = TestAjax.get(params.id)
        if (!testAjaxInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
            redirect(action: "list")
        }
        else {
            [testAjaxInstance: testAjaxInstance]
        }
    }

    def edit = {
        def testAjaxInstance = TestAjax.get(params.id)
        if (!testAjaxInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [testAjaxInstance: testAjaxInstance]
        }
    }

    def update = {
        def testAjaxInstance = TestAjax.get(params.id)
        if (testAjaxInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (testAjaxInstance.version > version) {
                    
                    testAjaxInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'testAjax.label', default: 'TestAjax')] as Object[], "Another user has updated this TestAjax while you were editing")
                    render(view: "edit", model: [testAjaxInstance: testAjaxInstance])
                    return
                }
            }
            testAjaxInstance.properties = params
            if (!testAjaxInstance.hasErrors() && testAjaxInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), testAjaxInstance.id])}"
                redirect(action: "show", id: testAjaxInstance.id)
            }
            else {
                render(view: "edit", model: [testAjaxInstance: testAjaxInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def testAjaxInstance = TestAjax.get(params.id)
        if (testAjaxInstance) {
            try {
                testAjaxInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'testAjax.label', default: 'TestAjax'), params.id])}"
            redirect(action: "list")
        }
    }
	def addTestData={
		for(int i=0;i<100;i++){
			new TestAjax(name:"aaa"+i).save();
			}
		new TestAjax(name:"aaaabbbb").save(flush:true);
		render(view: "addTestData")
		}
}
