var express = require("express");
var mysql = require('mysql');
var bodyParser = require("body-parser");
var HashMap = require('hashmap');
var conn = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  //password: 'zeroness120',
  password: '12345',
  database: 'my_db'
});
var app = express();
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

const PORT = 3000;
var map = new HashMap();

conn.connect(function(err) {
  if (!err) {
    console.log("Database is connect");
  } else {
    console.log("Error");
  }
});

app.post("/loca", function(req, res) {
  conn.query('SELECT * FROM location WHERE phonenum =? AND project =?', [req.body.phonenum, req.body.title], function(err, rows, fields) {
    if (err) {
      console.log("ERERERER");
    } else {
      if (rows.length == 0) {
        conn.query('INSERT INTO location VALUES (?,?,?)', [req.body.phonenum, req.body.point, req.body.title], function(err, result) {
          if (err) {
            console.log(err);
          } else {
            console.log("FINIsh");
          }
        });
      } else {
        console.log(req.body);
        conn.query('UPDATE location SET point =? WHERE phonenum =? AND project =?', [req.body.point, req.body.phonenum, req.body.title], function(err, result) {
          if (err) {
            console.log(err);
          } else {
            console.log("SU");
          }
        });
      }
    }
  });
});

app.post("/alarm", function(req, res) {
  var map = req.body.map;
  var arr = new Array();
  for (var i in map) {
    if (map.hasOwnProperty(i)) {
      arr[i] = map[i];
    }
  }
  conn.query('SELECT * FROM alarm WHERE master =? AND phonenum =? AND project =?', [req.body.master, req.body.phonenum, req.body.title], function(err, rows, fields) {
    if (err) {
      console.log("EROR");
    } else {
      if (rows.length == 0) {
        conn.query('INSERT INTO alarm VALUES (?,?,?,?,?,?,?,?)', [req.body.master, req.body.phonenum, req.body.title, map[7], map[5], map[3], map[1], req.body.finish], function(err, rows, fields) {
          if (err) {
            console.log("1");
            console.log(err);
          } else {
            console.log("ggg");
          }
        });
      } else {
        conn.query('UPDATE alarm SET alarm7 =?, alarm5 =?, alarm3 =?, alarm1 =? WHERE master =? AND phonenum =? AND project =?', [map[7], map[5], map[3], map[1], req.body.master, req.body.phonenum, req.body.title], function(err, result) {
          if (err) {
            console.log("2");
            console.log(err);
          } else {
            console.log("Alarm UPDATE");
          }
        });
      }
    }
  });
});

app.post("/table", function(req, res) {
  console.log(req.body.title);
  var map = req.body.map;
  conn.query('SELECT 1 FROM Information_schema.tables WHERE table_name=?', [req.body.title], function(err, rows) {
    if (err) {
      console.log("err");
    } else {
      if (rows.length == 0) {
        console.log("Create Table");
        var query = 'CREATE TABLE ' + [req.body.title] + ' (date varchar(20), votenum int(10))';
        conn.query(query, function(err, result) {
          if (err) {
            console.log(err);
          } else {
            for (var i in map) {
              if (map.hasOwnProperty(i)) {
                console.log(i + " " + map[i]);
                var query = 'INSERT INTO ' + [req.body.title] + ' (date, votenum) VALUES (?,?)'
                conn.query(query, [i, map[i]], function(err, rows, fields) {
                  if (err) {
                    console.log(err);
                    console.log("ERRPR");
                  } else {
                    console.log("GOOD");
                  }
                });
              }
            }
          }
        });
      } else {
        for (var i in map) {
          if (map.hasOwnProperty(i)) {
            console.log(i + " " + map[i]);
            var sql = 'UPDATE ' + [req.body.title] + ' SET votenum =? WHERE date =?';
            conn.query(sql, [map[i], i], function(err, result) {
              if (err) {
                console.log("5");
                console.log(err);
              } else {
                console.log("UPDATAE");
              }
            });
          }
        }
      }
    }
  });
});

app.post("/add", function(req, res) {
  console.log(req.body.project);
  conn.query('SELECT * FROM schedule WHERE master=? AND phonenum =? AND project =?', [req.body.master, req.body.phonenum, req.body.project], function(err, rows, fields) {
    if (err) {
      console.log("6");
      console.log(err);
    } else {
      if (rows.length == 0) {
        conn.query('INSERT INTO schedule VALUES (?,?,?,?,?,?,?,?)', [req.body.master, req.body.phonenum, req.body.project, req.body.meeting, req.body.start, req.body.finish, req.body.vote, req.body.peo], function(err, rows, fields) {
          if (!err) {
            console.log("EEEEEEEEEEEEE");
          } else {
            console.log('Insert');
          }
        });
      } else {
        console.log("HERE");
        conn.query('UPDATE schedule SET vote =? WHERE master=? AND phonenum =? AND project =?', [req.body.vote, req.body.master, req.body.phonenum, req.body.project], function(err, result) {
          if (err) {
            console.log("7");
            console.log(err);
          } else {
            console.log("VOte update");
          }
        });
      }
    }
  });
});

app.get("/phonenum/:num", function(req, res) {
  conn.query('SELECT * FROM user WHERE phonenum=?', [req.params.num], function(err, rows, fields) {
    if (err) {
      console.log("ERRRRRORORORORORO");
    } else {
      console.log(rows);
      if (rows.length == 0) {
        conn.query('INSERT INTO user (phonenum) VALUES(?)', [req.params.num], function(err, rows, fields) {
          if (err) {
            console.log("INSERT ERROR");
          } else {
            res.send(rows);
            console.log("INSERT SCCUESS");
          }
        });
      } else {
        conn.query('SELECT * FROM schedule WHERE phonenum=?', [req.params.num], function(err, rows, fields) {
          console.log(rows);
          res.send(rows);
          console.log("EXIST");
        })
      }
    }
  });
});

app.get("/project/:master/:name", function(req, res) {
  conn.query('SELECT * FROM schedule WHERE master =? AND project=?', [req.params.master, req.params.name], function(err, rows, fields) {
    if (err) {
      console.log("Project Error");
    } else {
      console.log(rows);
      res.send(rows);
    }
  });
});

app.get("/map/:title", function(req, res) {
  var sql = 'SELECT * FROM ' + [req.params.title];
  conn.query(sql, function(err, rows, fields) {
    if (err) {
      console.log("8");
      console.log(err);
    } else {
      res.send(rows);
      console.log(rows);
      console.log("ASDASD");
    }
  });
});

app.get("/gmap/:title/:num", function(req, res) {
  var sql = 'SELECT date FROM ' + [req.params.title] + ' ORDER BY votenum DESC, date ASC LIMIT ' + [req.params.num];
  conn.query(sql, function(err, rows, fields) {
    if (err) {
      console.log("9");
      console.log(err);
    } else {
      res.send(rows);
      console.log(rows);
    }
  });
});

app.get("/galarm/:mas/:pnum/:tit", function(req, res) {
  console.log(req.params);
  conn.query('SELECT alarm7,alarm5,alarm3,alarm1,project,finish FROM alarm WHERE master =? AND phonenum =? AND project =?', [req.params.mas, req.params.pnum, req.params.tit], function(err, rows, fields) {
    if (err) {
      console.log("10");
      console.log(err);
    } else {
      console.log(rows);
      res.send(rows);
      console.log("get alarm");
    }
  });
});

app.get("/gloca/:phonenum/:title", function(req, res) {
  conn.query('SELECT * FROM location WHERE phonenum =? AND project =?', [req.params.phonenum, req.params.title], function(err, rows, fields) {
    if (err) {
      console.log(err);
    } else {
      if (rows.length)
        console.log("됨");
      res.send(rows);
      console.log(rows);
    }
  });
});

var server = app.listen(PORT, function() {
  console.log("Server is runnung on port 3003");
});
