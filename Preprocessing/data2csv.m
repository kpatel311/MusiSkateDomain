function [ ] = data2csv( all_data )

    %all_data_num_rows = getNumRows(all_data)
    all_data_num_cols = getNumCols(all_data);
    fh = fopen('tricks_without_wrong.csv', 'w');

    for col = 1:all_data_num_cols
        %% All of the wrong tricks are stored in even columns in all_data.
        %% So, we can just ignore them.
        col_mod = mod(col, 2);
        if col_mod == 0
            continue
        %%    curr_label = 'wrong_trick';
        %%else
        %%    curr_label = all_data{1, col};
        end

        % Store current label to add to the csv file later
        curr_label = all_data{1, col};
        curr_trickset = all_data{2, col};
        curr_trickset_num_cols = getNumCols(curr_trickset);
        curr_trickset_num_rows = getNumRows(curr_trickset);

        for trickset_row = 2:curr_trickset_num_rows
            for trickset_col = 2:curr_trickset_num_cols
                %Check to make sure nonempty array
                %If nonempty write to csv
                curr_arr = curr_trickset{trickset_row, trickset_col};
                empty_bool = isempty(curr_arr);
                if empty_bool
                    continue
                else
                    writeArrToFile(fh, curr_arr, curr_label);
                end
            end
        end
    end
    fclose(fh);
end

function [ rows ] = getNumRows(data)
    size_arr = size(data);
    rows = size_arr(1);
end

function [ cols ] = getNumCols(data)
    size_arr = size(data);
    cols = size_arr(2);
end

%Write array to csv.
%If useLabel is true adds label to the line
function [] = writeArrToFile(filename, arr, label)
    %Print the first row with label
    row1 = arr(1, :);
    formatSpec = '%1f,%2f,%3f,%4f,%5f,%6f,%7f,%8f,%9f,%10f';
    formatSpec1 = [formatSpec ',' label];
    newLine = '\n';
    formatSpec1 = [formatSpec1 newLine];
    fprintf(filename, formatSpec1, row1);

    size_arr = size(arr);
    num_rows = size_arr(1);
    formatSpec = [formatSpec newLine];
    for row_num = 2:num_rows
        next_row = arr(row_num, :);
        fprintf(filename, formatSpec, next_row);
    end

    %row_remaining = arr(2:end,:);
    %formatSpec = '%1f,%2f,%3f,%4f,%5f,%6f,%7f,%8f,%9f,%10f,';
    %formatSpec1 = [formatSpec label];
    %newLine = '\n';
    %formatSpec = [formatSpec newLine];
    %formatSpec1 = [formatSpec1 newLine];

    %fprintf(filename, formatSpec1, row1);
    %fprintf(filename, formatSpec, row_remaining);
end
