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
		$accel_records = XYZRecord::where("session_id", "123")->where("sensor_id","1")->get();
		$light_records = XYZRecord::where("session_id", "123")->where("sensor_id","2")->get();
		
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
			'status' 	  => 200
		);

		return $response;
	}
}
