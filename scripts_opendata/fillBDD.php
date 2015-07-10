<?php
header('Content-Type: text/html; charset=utf-8');
$conf = array(
		'host' => 'localhost',
		'user' => 'projetPCD',
		'password' => 'tdajdrad',
		'database' => 'projetPCD'
		);

$error = "00:00:00";
$base = "https://encrypted.neness.net/";

$start=isset($_GET['start']) ? $_GET['start'] : null;
$parallel=isset($_GET['parallel']) ? $_GET['parallel'] : null;

if($start==null || $parallel==null)
{
	echo 'Passez par udpateBDD.html pour mettre à jour';
	exit;
}

if($parallel>1000)
	$parallel=100;
$mysqli = new mysqli($conf['host'], $conf['user'], $conf['password'], $conf['database']);

function execute($query)
{
	global $mysqli;
	return $mysqli->query($query);
}

function getData($query)
{
	global $mysqli;
	$result = execute($query);
	$data=array();
	while($row = $result->fetch_assoc())
		$data[] = $row;
	return $data;
}

function calculate($prev,$next,$cdist){
	$exp1=explode(':',$prev[0]);
	$exp2=explode(':',$next[0]);
	$interval = intval($exp2[0])*60+intval($exp2[1]) - (intval($exp1[0])*60+intval($exp1[1]));
	$calculated = intval($interval*($cdist-$prev[1])/($next[1]-$prev[1]))+(intval($exp1[0])*60+intval($exp1[1]));
	return intval($calculated/60).":".$calculated%60;
}

$count = 'SELECT DISTINCT COUNT(trip_id) AS c FROM opendata_trips';
$count = getData($count);
$count = $count[0]['c'];

if($start>$count)
	exit;
if($start+$parallel>$count)
	$parallel = $count-$start;

$trips=getData('SELECT DISTINCT (trip_id) FROM opendata_trips LIMIT ' . $start . ', '.$parallel);

foreach($trips as $trip){
	$times = getData('SELECT * FROM opendata_stop_times WHERE trip_id = "'.$trip['trip_id'].'" ORDER BY stop_sequence');
	
	$next=array();
	
	for($i=count($times)-1;$i>=0;$i--)
	{
		if($times[$i]['departure_time']!=$error){
			$nextTime = $times[$i]['departure_time'];
			$nextDist = $times[$i]['shape_dist_traveled'];
		}
		$next[$i][0]=$nextTime;
		$next[$i][1]=$nextDist;
	}
	
	for($i=0;$i<count($times);$i++){
		if($times[$i]['departure_time']!=$error){
			$prevTime = $times[$i]['departure_time'];
			$prevDist = $times[$i]['shape_dist_traveled'];
		}
		else{
			$newTime = calculate(array($prevTime,$prevDist),$next[$i],$times[$i]['shape_dist_traveled']);
			execute('UPDATE opendata_stop_times SET arrival_time = "'.$newTime.'", departure_time = "'.$newTime.'"
								WHERE trip_id = "' . $times[$i]['trip_id'] . '"
								AND stop_sequence = "' . $times[$i]['stop_sequence'] . '";');
			echo "<p>Précédent connu : ".$prevTime." - ".$prevDist."<br />";
			echo "Suivant connu : ".$next[$i][0]." - ".$next[$i][1]."<br />";
			echo "<strong>Calculé : ".$newTime." - ".$times[$i]['shape_dist_traveled']."</strong></p>";
		}
	}
}
$mysqli->close();

