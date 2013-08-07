package org.asdtiang.grails.paginateAjax

class AjaxPaginateService {
	static transactional = true
	
	def getResultMap(params,classInstance,hql,queryMap) {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if(!params.total){
			params.total=classInstance.executeQuery(hql,queryMap).size()
		}
		params.view=params.view==null?"list":params.view
		params.max = Math.min(params.max ? params.max as int : 1, 100)
		params.linkTotal = Math.min(params.linkTotal ? params.linkTotal as int : 2, 100)
		params.offset=params.offset?params.offset as int:0
		params.sort=params.sort==null?"id":params.sort
		params.order=params.order==null?"desc":params.order
		params.selectMax="true"
		def results =classInstance.executeQuery(hql+" order by ${params.sort}  ${params.order}",queryMap
		   ,[max:params.max,offset:params.offset])
		return [dataList:results,dataTotal:params.total]
	}
	def getResultMap(params,classInstance,hql) {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if(!params.total){
			params.total=classInstance.executeQuery(hql).size()
		}
		params.view=params.view==null?"list":params.view
		params.max = Math.min(params.max ? params.max as int : 1, 100)
		params.linkTotal = Math.min(params.linkTotal ? params.linkTotal as int : 2, 100)
		params.offset=params.offset?params.offset as int:0
		params.sort=params.sort==null?"id":params.sort
		params.order=params.order==null?"desc":params.order
		params.selectMax="true"
		def results =classInstance.executeQuery(hql+" order by ${params.sort}  ${params.order}"
		   ,[max:params.max,offset:params.offset])
		return [dataList:results,dataTotal:params.total]
	}
	def getResultMapSelfCount(params,classInstance,hql,countHql) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if(!params.total){
            params.total=classInstance.executeQuery(countHql).get(0)
        }
        params.view=params.view==null?"list":params.view
        params.max = Math.min(params.max ? params.max as int : 1, 100)
        params.linkTotal = Math.min(params.linkTotal ? params.linkTotal as int : 2, 100)
        params.offset=params.offset?params.offset as int:0
        params.sort=params.sort==null?"id":params.sort
        params.order=params.order==null?"desc":params.order
        params.selectMax="true"
        def results =classInstance.executeQuery(hql+" order by ${params.sort}  ${params.order}"
                ,[max:params.max,offset:params.offset])
        return [dataList:results,dataTotal:params.total]
    }
}
