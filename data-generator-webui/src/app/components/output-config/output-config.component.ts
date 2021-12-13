import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { OutputConfig } from './output-config';

@Component({
    selector: 'app-output-config',
    templateUrl: './output-config.component.html',
    styleUrls: ['./output-config.component.scss']
})
export class OutputConfigComponent implements OnInit {

    @Output() onChange = new EventEmitter<{ update_now: boolean, config: OutputConfig }>();

    available_formats = ['json', 'yaml', 'xml', 'sql', 'csv', 'template'];
    csv_default_separator = ['<tab>', ';', ','];
    count_max_warning = 1000;

    default_config: OutputConfig = {
        include_null: true,
        count: 5,
        format: 'json',
        pretty: true,
        object_name: 'my_object',
        table_name: 'my_table',
        separator: ';',
        gzip: false,
        single_file: false,
        filename_template: '',
        template: ['##',
            '## Documentation of velocity template here : https://velocity.apache.org/engine/devel/user-guide.html',
            '##',
            '#set ($title = "Random people list")',
            '',
            '$title',
            '===================',
            '#foreach($data in $list)',
            'id=$data.id',
            'name=$data.firstname $data.lastname',
            'age=$data.age',
            '#if($data.age >= 18)',
            'major=yes',
            '#else',
            'major=no',
            '#end',
            '---------',
            '#end'].join('\n'),
    };
    config: OutputConfig = { ...this.default_config }

    constructor() {
    }

    ngOnInit(): void {
        this.onChange.next({
            update_now: true,
            config: this.config
        });
    }

    updateParam(param: string, value: any) {
        // @ts-ignore
        this.config[ param ] = value;
        const update_now = !['template', 'object_name', 'table_name', 'separator'].includes(param);

        this.onChange.next({
            update_now,
            config: this.config
        });
    }
}
