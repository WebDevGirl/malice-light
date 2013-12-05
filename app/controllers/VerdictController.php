<?php

class VerdictController extends \BaseController {

	/**
	 * Display a listing of the resource.
	 *
	 * @return Response
	 */
	public function index()
	{
		return "Nothing Yet";
	}


	/**
	 * Display the specified resource.
	 *
	 * @param  int  $id
	 * @return Response
	 */
	public function show($id)
	{	

		ob_start(); //-- Start Output surpression

		/* Get Data To Analyze */
		$accel_records = XYZRecord::where("session_id", $id)->where("sensor_id","1")->get();
		$light_records = XYZRecord::where("session_id", $id)->where("sensor_id","2")->get();

		/* Prep Image URLS */
		$img_accell_x_URI = public_path()."/images/".$id."_accell_x.png";
		$img_light_URI = public_path()."/images/".$id."._light.png;";

		/*****************************************
		*	LIGHT SENSOR ANALYSIS
		*****************************************/
		/* Set Time at first point */
		$init_time = $accel_records[0]['record_float'];


		/* Get points for x vector for LIGHT sensor */
		$x_points = "";
		foreach($light_records as $rec) {
			$x_points .= ($rec['record_float'] - $init_time) . ", "; 
		}
		$x_points = substr($x_points,0,strlen($x_points)-2); // remove tailing ,

		
		/* Get points for y vector for LIGHT sensor */
		$y_points = "";
		foreach($light_records as $rec) {
			$y_points .= $rec['x'] . ", "; 
		}
		$y_points = substr($y_points,0,strlen($y_points)-2); // remove tailing ,


		/* Build Octave Script - Graph */
		$octave_script  = "x=[${x_points}];\n";
		$octave_script .= "y=[${y_points}];\n";
		$octave_script .= "plot(x,y);\n";
		$octave_script .= "print -dpng '".$img_light_URI."'";

		/* Set Script Filename */
		$octave_script_path = storage_path()."/cache/file.m";

		/* Print To File */
		touch($octave_script_path);
		File::put($octave_script_path, $octave_script);

		/* System Call - Forgive this transgression */
		system(' octave --silent --eval "source(\''.$octave_script_path.'\');"');

		
		/*****************************************
		*	ACELLEROMTER DATA ANALYSIS
		*****************************************/
		/* Get points for x vector for LIGHT sensor */
		$x_points = "";
		foreach($light_records as $rec) {
			$x_points .= ($rec['record_float'] - $init_time) . ", "; 
		}
		$x_points = substr($x_points,0,strlen($x_points)-2); // remove tailing ,

		
		/* Get points for y vector for LIGHT sensor */
		$x_y_points = "";
		foreach($light_records as $rec) {
			$x_y_points .= $rec['x'] . ", "; 
		}
		$x_y_points = substr($x_y_points,0,strlen($x_y_points)-2); // remove tailing ,

		/* Get Existing Octave Script */
		 $octave_partition_points_path = storage_path()."/octave/partition_points.m";
		 $octave_partition_points_func = File::get($octave_partition_points_path);
 
		 /* Put path to image folder in Octave Script */
		 $octave_partition_points_func = str_replace("===FILE_PATH_HERE===",$img_accell_x_URI,$octave_partition_points_func);
		/* Build Octave Script - Graph */
		$octave_script  = "x=[${x_points}];\n";
		$octave_script .= "y=[${x_y_points}];\n";
		$octave_script .= $octave_partition_points_func . "\n";
		$octave_script .= "partition_points(x,y);\n";
		
		/* Print To File */
		touch($octave_script_path);
		File::put($octave_script_path, $octave_script);

		/* System Call - Forgive this transgression */
		system(' octave --silent --eval "source(\''.$octave_script_path.'\');"');


		ob_end_clean(); //-- End Output surpression

		$dataArray[] = array(
			'sensor_id' => 1,
			'sensor_name' => "Accellerometer",
			'record_count' => sizeof($accel_records->toArray())
		);

		$dataArray[] = array(
			'sensor_id' => 2,
			'sensor_name' => "Light",
			'record_count' => sizeof($light_records->toArray())
		);


		/* Prepare response with new resource data */
		$response = array(
			'id' 		  => 'Success',
			'description' => 'Data successfully retrieved',
			'data' 		  => $dataArray,
			'img_accell_x_URI' => $img_accell_x_URI,
			'img_light_URI' => $img_light_URI,
			'status' 	  => 200
		);

		return $response;
	}
}
