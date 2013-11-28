<?php

use Illuminate\Database\Migrations\Migration;

class CreateXyzdataTable extends Migration {

	/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
		Schema::create('xyzrecords', function($table)
		{
		    $table->increments('id');
		    $table->integer("sensor_id")->unsigned();
		    $table->float("x");
		    $table->float("y");
		    $table->float("z");
		    $table->timestamp("record_date");
		    $table->double("record_float");
		    $table->timestamps();
		});
	}

	/**
	 * Reverse the migrations.
	 *
	 * @return void
	 */
	public function down()
	{
		Schema::drop('xyzrecords');
	}

}
