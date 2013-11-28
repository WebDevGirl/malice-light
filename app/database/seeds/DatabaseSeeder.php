<?php

class DatabaseSeeder extends Seeder {

	/**
	 * Run the database seeds.
	 *
	 * @return void
	 */
	public function run()
	{
		Eloquent::unguard();

		XYZRecord::create(array(
			'sensor_id' => 1,
			'x' => 8.3,
			'y' => 1.2,
			'z' => 2.3,
			'record_date' => date( 'Y-m-d H:i:s', time()),
			'record_float' =>  microtime(true)
		));

		XYZRecord::create(array(
			'sensor_id' => 1,
			'x' => 10.3,
			'y' => 11.2,
			'z' => 21.3,
			'record_date' => date( 'Y-m-d H:i:s', time()),
			'record_float' =>  microtime(true)
		));
		error_log(microtime(true));
	}

}