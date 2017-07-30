(function(){
  function startInterVal(isFirst) {
    return setInterval(function(){ getWhitelistedDevices(); }, 3000);
  }

  var interval = startInterVal(true);

  function clearMainInterval() {
      clearInterval(interval);
  }

  var isRefreshEnabled = true;
  var isMultiModeEnabled = true;

  function setRefresh(enabled) {
    isRefreshEnabled = enabled;
  }

  function restartMainInterval() {
    var isEnabled = isRefreshEnabled;
    if (isEnabled) {
      interval = startInterVal(false);
    } else {
      clearMainInterval();
    }
  }

  var urlMapping = {
    whitelistedDevices : "/enabled-device",
    setVolume : "/set-volume",
    mute : "/mute",
    unmute : "/unmute"
  }

  var token = "{}";

  function getCard(item) {
    var muted = item['volumeInformation']['muted'] ? "muted" : "";

    return '<div class="col s12 m6 '+ muted +'">' +
                     '<div class="card">' +
                       '<div class="card-content">' +
                         '<span class="card-title name">'
                           + item['deviceName'] +
                         '</span>' +
                         '<span class="card-value value">'
                           + item['volumeInformation']['volume'] +
                         '</span>' +
                         '<p class="range">' + token + '</p>' +
                       '</div>' +
                       '<div class="card-action">' +
                         '<a class="mute" href="#">Mute</a>' +
                         '<a class="unmute" href="#">Unmute</a>' +
                       '</div>' +
                     '</div>' +
                   '</div>';
  }

  function getCardRow() {
    return '<div class="row"></div>';
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
      $cardRow = $(getCardRow());

      data.forEach(function(item) {
        var range = volumeRange.replace(token, item['volumeInformation']['volume']);
        var card =  getCard(item);
        card = card.replace(token, range);
        var $card = $(card);
        $card.attr('id', item['deviceName']);
        $cardRow.append($card);
      });

      $whitelistDiv.append($cardRow);
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

  function mute(deviceName) {
    var url = urlMapping.mute + "/" + deviceName;
    clearMainInterval();

    $.get(url, function(data) {
      $('#' + deviceName).addClass("muted");
      restartMainInterval();
    });
  }

  function unmute(deviceName) {
    var url = urlMapping.unmute + "/" + deviceName;
        clearMainInterval();

        $.get(url, function(data) {
          $('#' + deviceName).removeClass("muted");
          restartMainInterval();
        });
  }

  var $whitelistDiv = $('#whitelist');

  $whitelistDiv.on('change', '.volume-control' ,function(event) {
    $target = $(event.target);

    volume = $target.val();
    device = $target.parent().siblings('span.name').first().text();

    changeVolume(device, volume);
  });

  $whitelistDiv.on('click', '.card-action a' ,function(event) {
      event.preventDefault();
      var $target = $(event.target);
      var device = $target.parents().eq(2).attr('id');

      if ($target.hasClass("mute")) {
        mute(device);
      } else if ($target.hasClass("unmute")) {
        unmute(device);
      }
      return false;
    });

  var $pushdown = $('#pushdown');

  $pushdown.on('change', 'input[type=checkbox]' ,function(event) {
      $target = $(event.target);
      var method = $target.attr('id');
      var enabled = event.target.checked;

      if (method === "refresh") {
        clearMainInterval();
        setRefresh(enabled);
        restartMainInterval();
      }

      if (method === "multimodule") {
        // TODO: add functionality for switchig back to base or not
      }
    });

  $('#refresh').prop('checked', isRefreshEnabled);
  $('#multimode').prop('checked', isMultiModeEnabled);

  // startup with a fancy delay ;-) and loading all the way :)
  setTimeout(function(){ getWhitelistedDevices(); }, 1000);

})();