<?php

namespace App\Services\Validators;

abstract class Validator {

	/**
	 * Container for the data that needs to be validated
	 * @var array
	 */
	protected $data;

	/**
	 * Validation errors
	 * @var array
	 */
	public $errors;

	/**
	 * Validation rules
	 * @var array
	 */
	public static $rules;

	/**
	 * Initialize validator
	 * @param array $data
	 */
	public function __construct($data = null) {

		$this->data = $data ?: \Input::all();
	}

	/**
	 * Check if validation passes
	 * @return bool
	 */
	public function passes() {

		$validation = \Validator::make($this->data, static::$rules);

		if ($validation->passes()) {

			return true;
		}

		$this->errors = $validation->messages();

		return false;
	}

	public function fails() {

		$validation = \Validator::make($this->data, static::$rules);

		if ($validation->fails()) {

			$this->errors = $validation->messages();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets data to be validated (input::all() by default)
	 * @internal only use this when you can't use the constructor (ie: loop)
	 */
	public function setData($data) {
		
		$this->data = $data;
	}


	/**
	 * Applys the rules to be used on the data
	 * @internal EXAMPLE OF USE (IE: in ClassesController): $validation->applyRules(ClassesValidator::$update_rules);
	 */
	public function applyRules($newRules = null) {
		
		static::$rules = $newRules;
	}
}