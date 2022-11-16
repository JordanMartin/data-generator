import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {DateAgoPipe} from '../../pipes/date-ago.pipe'

@Component({
  selector: 'app-date-ago',
  template: '<span>{{ dateAgo }}</span>'
})
export class DateAgoComponent implements OnInit, OnDestroy {

  @Input() refresh: number = 0;
  @Input() set date(date: Date | undefined) {
    this._date = date;
    this.updateDateAgo();
  }
  private _date?: Date;

  dateAgo: string = '';
  private refreshIntervalId?:  ReturnType<typeof setTimeout>;

  constructor() { }

  ngOnInit(): void {
    this.updateDateAgo();
    if (this.refresh > 0) {
      this.refreshIntervalId = setInterval(() => this.updateDateAgo(), this.refresh * 1000);
    }
  }

  updateDateAgo() {
    this.dateAgo = new DateAgoPipe().transform(this._date);
  }

  ngOnDestroy(): void {
    if (this.refreshIntervalId) {
      clearInterval(this.refreshIntervalId);
    }
  }
}
