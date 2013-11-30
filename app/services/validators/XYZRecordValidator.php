<?php 

namespace App\Services\Validators;
 
class XYZRecordValidator extends Validator {
	
	public static $create = array(
		'session_id' 			=> 'required',
		'sensor_id'		=> 'required|numeric',
		'x'				=> 'required|numeric',
		'y'				=> 'numeric',
		'z'				=> 'numeric',
		'record_date'	=> 'required',
		'record_float'	=> 'required',
	);
}
