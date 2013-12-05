<?php 

namespace App\Services\Validators;
 
class XYZRecordValidator extends Validator {
	
	public static $create = array(
		'sequence_number' 			=> 'required|numeric',
		'session_id' 			=> 'required',
		'sensor_id'		=> 'required|numeric',
		'x'				=> 'required|numeric',
		'y'				=> 'numeric|numeric',
		'z'				=> 'numeric|numeric',
		'record_date'	=> 'required',
		'record_timestamp'	=> 'required',
	);
}
