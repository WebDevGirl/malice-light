%y=[0,8,0,-8,0,4,4,4,4,4,4,4,4,4,4,4,4,0,-8,0,8,0,4,2,0,0,8,0,8,2,2,2,2,2,2,2,2,2,2,2,2,0,8,4,2,0,8,-8,0,0,1,12,3,0,-8,1,10,1,0,1,10,0,0,10,10,0,0,10,10,4,4,4,4,4,4,4,4,4,4,4,4,10,1,-9,1,0,10,2,4,2,4,2,4,2,4,2,4,2,4,2,4,2,4,2,4,2,4,2,4,10,-2,10,-2,10];
%x=[1:size(y)(2)];

function partition_points(x_points,y_points)

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

        %add to partition if not ignoring
        if (is_ignoring==0)
            curr_x_parts(end+1) = x_points(counter); 
            curr_y_parts(end+1) = i; 
        endif

        %index coutner
        counter=counter+1;
    endfor

    if (is_ignoring == 0)
        % Attach last partition if not ignoring points
        all_x_parts(p_num) = curr_x_parts;
        all_y_parts(p_num) = curr_y_parts;
    else
        % One less partition then counted
        p_num = p_num - 1
    endif   

    % Plot All the Graphcs
    graph_rows = (ceil(p_num/2))+1
    
    % plot all points on top
    subplot(graph_rows,2,1:2)
    plot(x_points,y_points)

      
    i_counter = 1;
    g_counter = 3;
    for i = 1:p_num    
        subplot(graph_rows,2,(i_counter+2))    
        plot(all_x_parts{1,i_counter},all_y_parts{1,i_counter})
        i_counter=i_counter+1
    endfor

    print -dpng '===FILE_PATH_HERE===';

endfunction

partition_points(x,y);
