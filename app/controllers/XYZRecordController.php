<?php
use App\Services\Validators\XYZRecordValidator;
class XYZRecordController extends \BaseController {

	/**
	 * Display a listing of the resource.
	 *
	 * @return Response
	 */
	public function index()
	{
		$response = array(
			'id'		=> 'Success',
			'data'		=> XYZRecord::all()->toArray(),
			'status'	=> 200
		);

		return Response::make($response, 200);
	}

	/**
	 * Store a newly created resource in storage.
	 * @todo permissions
	 * @return Response
	 */
	public function store()
	{
		/* Check Permissions */
		//TODO

		/* Validate Prep */
		$validator = new XYZRecordValidator;				
		$validator->applyRules(XYZRecordValidator::$create);

		/* Validate Data - return errors if it failed */
		if ($validator->fails()) {

			$response = array(
				'id' 		  => 'Error',
				'description' => $validator->errors->first(),
				'errors'      => $validator->errors->toArray(),
				'status'      => 400
			);

			/* Return response errors */
			return Response::make($response, 400);
		}

		/* Prepare to insert with valid data */
		$xyz = new XYZRecord;

		/* Only fill course object with keys from rules array */
		$xyz->fill(Input::only(array_keys(XYZRecordValidator::$rules)));
		$xyz->save();

		/* Prepare response with new resource data */
		$response = array(
			'id' 		  => 'Success',
			'description' => 'The XYZRecordValidator was successfully added.',
			'data' 		  => $protocol->toArray(),
			'status' 	  => 200
		);

		return Response::make($response, 200);
	}

	/**
	 * Display the specified resource.
	 *
	 * @todo permissions
	 * @param  int  $id
	 * @return Response
	 */
	public function show($id)
	{
		$response = array(
			'id'		=> 'Success',
			'data'		=> XYZRecord::findOrFail($id)->toArray(),
			'status'	=> 200
		);

		return Response::make($response, 200);
	}

}