(function(){
  var urlMapping = {
    whitelistedDevices : "/enabled-device",
    setVolume : "/set-volume"
  }

  var token = "{}";

  function getWhitelistedDevices() {
    var volumeRange = '<p class="range-field">' +
                           '<input type="range" class="volume-control" min="0" max="100" value="'
                           + token +
                           '" />' +
                       '</p>';


    $.getJSON(urlMapping.whitelistedDevices, function(data) {
      $whitelistDiv = $('#whitelist');
      $whitelistDiv.html("");
      $ul = $("<ul></ul>");

      data.forEach(function(item) {
        $range = $(volumeRange.replace(token, item['volume']));
        $range.attr('id', item['deviceName']);
        $li = $("<li><h4>" +
                    " <span class='name'>"
                      + item['deviceName'] +
                    " </span> " +
                    " <span class='value'>"
                      + item['volume'] +
                    "</span>" +
                "</h4></li>");
        $li.append($range);
        $ul.append($li);
      });

      $whitelistDiv.append($ul);
    })
  }

  function changeVolume(deviceName, volumeLevel) {
    var url = urlMapping.setVolume + "/" + deviceName + "/" + volumeLevel;

    $.get(url, function(data) {
      $('#' + deviceName).prev().children('span.value').first().text(volumeLevel);
    });
  }

  var $whitelistDiv = $('#whitelist');

  $whitelistDiv.on('change', '.volume-control' ,function(event) {
    $target = $(event.target);

    volume = $target.val();
    device = $target.parent().attr('id');

    changeVolume(device, volume);
  });

  function startInterVal(isFirst) {
    return setInterval(function(){ getWhitelistedDevices(); }, 4000);
  }

  // startup with a fancy delay ;-) and loading all the way :)
  setTimeout(function(){ getWhitelistedDevices(); }, 1500);
  var interval = startInterVal(true);

  function clearMainInterval() {
      clearInterval(interval);
  }

  function restartMainInterval() {
    interval = startInterVal(false);
  }
})();