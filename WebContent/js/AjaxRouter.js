 
        function AjaxRouter() {
          
            this.globalCache = {};
            this.testing = false;
            this.isfree = false;
            this.callbacks = {};
            this.buffer = "";
            this.ajaxRequestPending = false;
            this.$body = $('body');
            this.isfree = false;
            this.timeout = 360000;
            this.callbacks = {};
            this.buffer = "";
            this.ajaxRequestPending = false;
            $.ajaxRouterInstance = this;
        }
        AjaxRouter.prototype.JSON_stringify = function (s) {
            var json = JSON.stringify(s);
            return json.replace(/[\u007f-\uffff]/g, function (c) {
                return '\\u' + ('0000' + c.charCodeAt(0).toString(16)).slice(-4);
            });
        };
        AjaxRouter.prototype.responseHandler = function (response) {
            this.isfree = true;
            console.log("responseHandler: response %o", response);
            this.ajaxRequestPending = false;
             
            var m_request_type = response.m_request_type;
            if (this.callbacks[m_request_type]) {
                this.callbacks[m_request_type](response);
            }
        };
         
        AjaxRouter.prototype.ajaxrequest = function (request, url, timeout) {
            if (this.ajaxRequestPending)
                return;
            var mainclass = this;
           
            var REQUEST_JSON = this.JSON_stringify(request);
            var reqParams =  { "REQUEST_JSON": REQUEST_JSON } ;
                
            var ajaxForm = {
                url: url,
                type: "POST",
                cache: false,
                timeout: this.timeout,
                data: reqParams,
                success: function (response) {
                    mainclass.$body.css("cursor", "default");
                    mainclass.ajaxRequestPending = false;
                    mainclass.responseHandler(response);
                },
                error: function (x, t, m) {
                    mainclass.$body.css("cursor", "default");
                    mainclass.ajaxRequestPending = false;
                    if (t === "timeout") {
                        displayAlertMessage("Communication with the server timed out");
                    }
                    else {
                        displayAlertMessage("Communication with the server failed: " + t);
                    }
                }
            };
          
            
            this.ajaxRequestPending = true;
            this.$body.css("cursor", "progress");
            $.ajax(ajaxForm);
        };
      
        AjaxRouter.prototype.sendrequest = function (request, callback, timeout) {
         
            // var jsonstr = this.JSON_stringify(request);
            // console.log("sending text: " + jsonstr);
            this.callbacks[request.m_request_type] = callback;
            try {
                // this.ajaxrequest({ REQUEST_JSON: jsonstr }, "/AjaxJsonRequest");
                this.ajaxrequest(request, "/HultonHotelReservation/AjaxJsonRequest", null, timeout);
            }
            catch (e) {
                this.ajaxRequestPending = false;
                displayAlertMessage("Communication with the server failed: " + e);
            }
        };
        
        function displayAlertMessage(message) {
            var dialogMessage = $('<div id="dialog-alert-message" title=""> </div>');
            dialogMessage.title = message;
            dialogMessage.html(message);
            dialogMessage.dialog({
                modal: true,
                title: message,
                buttons: {
                    Ok: function () {
                        $(this).dialog("close");
                    }
                }
            });
        }
  
        function displayComfirmMessage(message) {
            var dialogMessage = $('<div id="dialog-alert-message" title=""> </div>');
            dialogMessage.title = message;
            dialogMessage.html(message);
            return dialogMessage.dialog({
                modal: true,
                title: message,
                buttons: {
                    Ok: function () {
                        $(this).dialog("close");
                        return true;
                    },
                    Cancel: function () {
                        $(this).dialog("close");
                        return false;
                    }
                }
            });
        }
     
