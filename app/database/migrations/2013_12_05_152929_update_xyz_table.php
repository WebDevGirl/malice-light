<?php

use Illuminate\Database\Migrations\Migration;

class UpdateXyzTable extends Migration {

/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
		Schema::table('xyzrecords', function($table) {	    			
    			$table->bigInteger('sequence_number');
    			$table->dropColumn('record_float');
    			$table->bigInteger('record_timestamp');
		});
	}

	/**
	 * Reverse the migrations.
	 *
	 * @return void
	 */
	public function down()
	{
		Schema::table('xyzrecords', function($table) {
                $table->dropColumn('sequence_number');
                $table->double("record_float");
                $table->dropColumn('record_timestamp');
		}); 
	}

}
