var express = require('express');
var app = express();
var path = require('path');
app.use(express.static(path.join(__dirname, 'public')));
var server = require('http').Server(app);
var io = require('socket.io')(server);
var logger = require('morgan');
var bodyParser = require('body-parser');

var router = express.Router();

var data;

router.get('/', function(req, res) {

});


router.post('/', function(req, res) {
  data = req.body;
  console.log(data);
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
    console.log(data);
  });
  socket.on('message', function (data) {
    console.log(data);
  });
});