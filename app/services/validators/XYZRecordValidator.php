<?php 

namespace App\Services\Validators;
 
class XYZRecordValidator extends Validator {
	
	public static $create = array(
		'sensor_id'		=> 'required|numeric',
		'x'				=> 'required|numeric',
		'y'				=> 'required|numeric',
		'z'				=> 'required|numeric',
		'record_date'	=> 'required|numeric',
		'record_float'	=> 'required|numeric',
	);
}