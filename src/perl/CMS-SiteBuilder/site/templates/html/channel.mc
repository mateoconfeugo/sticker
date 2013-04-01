<%args>
 $uri => $element->get_value('uri')
 $type => $element->get_value('type')
 $data_type => $element->get_value('data type')
 $target => $element->get_element('target')
 $channel_name => $element->value('channel name')
</%args>

<script>
$(document).ready(function () {

  post = Object.spawn(s123.ajax.post, {
    'url': <% $uri %>,
    'type': <% $type %>,
    'dataType":<% $data_type %>,
    'success_callback': function (data) {
        if (data.redirect_uri) {
          window.location.href = data.redirect_uri;
        }
        else {
          this.response_cb_function(data);
        }
   },
   response_callback: function() {},   
 });
  $.channels.<% $channel_name %> = post;
});
</script>


