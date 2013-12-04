% y=[0,8,0,-8,0,4,4,4,4,0,-8,0,8,0,4,2,0,0,8,0,8,2,2,2,2,2,2,0,8,4,2,0,8,-8,0,0,1,1,1,1,12,3,0,-8,1,1,1,1,1,1,1,1,1,1,1,10,1,0,1,10,0,0,10,10,0,0,10,10,4,4,4,4,4,4,4,4,4,4,4,4,10,1,-9,1,0,10,4,4,4,4,4,4,4,4,4,4,4,4,4,0,5,0,5,0,5,10,1,1,1,1,1,10,5,0,1,2,3,4,5,6,7,8,10,9,8,7,6,5,4];
% x=[1:size(y)(2)];

function partition_points(x_points,y_points,file_path)

    % loop_counter
    counter=1;
    
    % Steps and how many before partitioning

    steps=1;
    max_steps = 3;

    %track all x,y parititions in cell array
    all_y_parts = {};
    all_x_parts = {};

    %track a single x,y parition
    curr_x_parts=[];
    curr_y_parts=[];

    % Counter to determine partition number 
    p_num=1;

    %ignore coord within threshold
    is_ignoring = 0;

    %threshhold config
    START_THRESHOLD = 1;
    END_THRESHOLD = 4;
   
    % Loop through all y values 
    for i=y_points

        % THREADHOLD CHECK
        if (i>=START_THRESHOLD && i<=END_THRESHOLD)

            %within threadhold, start counting           
            if (steps >= max_steps && is_ignoring==0)               
                all_y_parts(p_num) = curr_y_parts;
                all_x_parts(p_num) = curr_x_parts;
               
                curr_y_parts = [];
                curr_x_parts = [];
                
                p_num=p_num+1;
                is_ignoring=1;
            endif

            steps=steps+1 
        else
            %not in threadhold - reset
            steps=1;
            is_ignoring=0;

        endif

        %add to partition
        curr_x_parts(end+1) = x_points(counter); 
        curr_y_parts(end+1) = i; 

        %index coutner
        counter=counter+1;
    endfor

    % Attach last partition if not ignoring points
    if (is_ignoring == 0)
       
        all_x_parts(p_num) = curr_x_parts;
        all_y_parts(p_num) = curr_y_parts;

    endif   


    % Plot All the Graphcs
    i_counter = 1;

    for i = 1:p_num
        subplot((p_num/2),2,i_counter)
        plot(all_x_parts{1,i_counter},all_y_parts{1,i_counter})
        i_counter=i_counter+1
    endfor

    print -dpng file_path;

endfunction

% partition_points(x,y, '/www/var/file.png');
