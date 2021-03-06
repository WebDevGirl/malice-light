<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

/*************************************/
/* 	 REGISTER RESOURCE CONTROLLERS   */
/*************************************/
Route::resource('api/v1/xyzrecords', 'XYZRecordController');
Route::resource('api/v1/verdicts', 'VerdictController');

/* Store Multiple resources */
Route::post('api/v1/xyzrecords/bulk', 'XYZRecordController@storeBulk'); 