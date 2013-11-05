var MI_CLUSTER = MI_CLUSTER || {};
(function($) {
	
    var _registry = "http://www.ebi.ac.uk/Tools/webservices/psicquic/registry/registry?action=ACTIVE%26format=xml"
    var _proxy = 'proxy.php';//"http://dachstein.biochem.mpg.de:8080/cgi-bin/test.cgi"
    var _service_url;
    
    MI_CLUSTER.init = function(service_url){
        _service_url = service_url;
    }
    
    MI_CLUSTER.cluster = function(params, fn){
        var url = _service_url+'/cluster?q='+params.q+'&service='+params.service.join()+
                        '&mapping='+params.mapping.join()+'&score='+params.score;
        
        MI_CLUSTER.ajax(url, fn, false);
    }
    
    MI_CLUSTER.clusterURL = function(params, fn){
        var url = _service_url+'/cluster?serviceurl='+params.serviceurl+
                        '&mapping='+params.mapping.join()+'&score='+params.score;
        
        MI_CLUSTER.ajax(url, fn, false);
    }
    
    MI_CLUSTER.status = function(jobId, fn){
        MI_CLUSTER.ajax(_service_url+"/status/"+jobId, fn);
    }
    
    MI_CLUSTER.ajax = function(url, fn, useProxy){
        
        var tmp = (useProxy == true) ? _proxy+'?url='+url : url;
        
        var params = {
            url: tmp,
            success: fn
		}
		jQuery.ajax(params);
    }
    
    MI_CLUSTER.getServers = function(fn){
        MI_CLUSTER.ajax(_registry, fn, true);
    }
    
})(jQuery);