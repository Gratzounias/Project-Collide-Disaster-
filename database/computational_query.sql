SELECT x.id as term1, y.id as term2 FROM 
(SELECT * FROM  `table` t1 where t1.`source`= '4fa2064ab69ede57' and now()-dt < 1 order by t1.dt desc limit 1) as x
cross join 
(SELECT * FROM  `table` t2 where t2.`source`= 'a07ea291c136cb8f' and now()-dt < 1 order by t2.dt desc limit 1) as y
where abs(timediff(x.dt, y.dt)) < 1 and (x.collide= 0 or y.collide = 0)