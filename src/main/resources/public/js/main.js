(function(){
  function startInterVal(isFirst) {
    return setInterval(function(){ getWhitelistedDevices(); }, 4000);
  }

  var interval = startInterVal(true);

  function clearMainInterval() {
      clearInterval(interval);
  }

  function restartMainInterval() {
    interval = startInterVal(false);
  }

  var urlMapping = {
    whitelistedDevices : "/enabled-device",
    setVolume : "/set-volume"
  }

  var token = "{}";

  function getCard(item) {
    return '<div class="row">' +
        '<div class="col s12 m6">' +
          '<div class="card">' +
            '<div class="card-content">' +
              '<span class="card-title name">'
                + item['deviceName'] +
              '</span>' +
              '<span class="card-value value">'
                + item['volume'] +
              '</span>' +
              '<p class="range">' + token + '</p>' +
            '</div>' +
            '<div class="card-action">' +
              '<a href="#">Mute</a>' +
              '<a href="#">Unmute</a>' +
            '</div>' +
          '</div>' +
        '</div>' +
      '</div>';
  }

  function getWhitelistedDevices() {
    var volumeRange = '<p class="range-field">' +
                           '<input type="range" class="volume-control" min="0" max="100" value="'
                           + token +
                           '" />' +
                       '</p>';


    $.getJSON(urlMapping.whitelistedDevices, function(data) {
      $whitelistDiv = $('#whitelist');
      $whitelistDiv.html("");

      data.forEach(function(item) {
        var range = volumeRange.replace(token, item['volume']);
        var card =  getCard(item);
        card = card.replace(token, range);
        var $card = $(card);
        $card.attr('id', item['deviceName']);
        $whitelistDiv.append($card);
      });
    })
  }

  function changeVolume(deviceName, volumeLevel) {
    var url = urlMapping.setVolume + "/" + deviceName + "/" + volumeLevel;
    clearMainInterval();

    $.get(url, function(data) {
      $('#' + deviceName).find('span.value').first().text(volumeLevel);
      restartMainInterval();
    });
  }

  var $whitelistDiv = $('#whitelist');

  $whitelistDiv.on('change', '.volume-control' ,function(event) {
    $target = $(event.target);

    volume = $target.val();
    device = $target.parent().siblings('span.name').first().text();

    console.log(device);

    changeVolume(device, volume);
  });

  // startup with a fancy delay ;-) and loading all the way :)
  setTimeout(function(){ getWhitelistedDevices(); }, 1500);

})();