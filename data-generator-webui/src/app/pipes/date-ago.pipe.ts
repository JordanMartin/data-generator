/**
 * Inspired from https://stackoverflow.com/a/67430249
 */
import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'dateAgo',
    pure: true
})
export class DateAgoPipe implements PipeTransform {

    readonly justNowThreshold = 10;

    transform(value: any, args?: any): any {

      if (!value) {
        return '';
      }

      const seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
      if (seconds < this.justNowThreshold) {
        return 'Just now';
      }

      const intervals = {
          'year': 31536000,
          'month': 2592000,
          'week': 604800,
          'day': 86400,
          'hour': 3600,
          'minute': 60,
          'second': 1
      };
      let counter;
      for (const i in intervals) {
          counter = Math.floor(seconds / intervals[i as keyof typeof intervals]);
          if (counter === 1) {
              return counter + ' ' + i + ' ago'; // singular (1 day ago)
          } else if (counter > 1) {
              return counter + ' ' + i + 's ago'; // plural (2 days ago)
          }
      }

      return value;
    }
}
