<?php

class XYZRecord extends Eloquent {
	
	protected $table = 'xyzrecords';

	// The fillable property specifies which attributes can be mass-assignable.
	protected $fillable = array('sensor_id','x','y','z','record_date','record_float', 'session_id');

}
