var express = require('express');
var app = express();
var path = require('path');
app.use(express.static(path.join(__dirname, 'public')));
var server = require('http').Server(app);
var io = require('socket.io')(server);
var logger = require('morgan');
var bodyParser = require('body-parser');

var router = express.Router();

var data = [];
var socketData;
var distanceToRift;

// data is an array of events
// The format is
// { t: timestamp, e: eventType, x: horizontal distance, y: vertical distance }
router.get('/', function(req, res) {
  res.json(data);
  data = [];
});


router.post('/', function(req, res) {
  data.push(req.body);
  console.log(data);
  res.send(200);
});

router.get('/data', function(req, res) {
  // res.json(socketData)
  res.json(distanceToRift);
});

router.post('/data', function(req, res) {
  distanceToRift = req.body;
  res.send(200);
});

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());

app.use('/', router);

server.listen(8080);


io.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('events', function (data) {
    if (data !== socketData) {
      console.log(data);
    }
    socketData = data;
  });
  socket.on('message', function (data) {
    console.log(data.length);
    socketData = data;
  });
});
