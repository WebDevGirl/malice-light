<?php

use Illuminate\Database\Migrations\Migration;

class UpdateXyzrecordsTable extends Migration {

	/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
		Schema::table('xyzrecords', function($table) {		
    			$table->string('session_id');
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
                $table->dropColumn('session_id');
		}); 
	}

}
