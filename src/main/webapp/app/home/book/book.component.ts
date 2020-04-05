import { Component, Input, OnInit } from '@angular/core';
import { BASE_URL_BOOKS, NO_IMAGE } from 'app/app.constants';
import { BooksService } from 'app/services/books.service';
import { MatDialog } from '@angular/material/dialog';
import { SuggestionComponent } from 'app/home/suggestion/suggestion.component';

@Component({
  selector: 'jhi-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
  @Input()
  filename = '';
  suggestions: string[] = [];
  displaySuggestion = false;
  data: any;
  title = '';
  author = '';
  imageSrc = '';
  subjects: string[] = [];
  languages: string[] = [];

  constructor(private booksService: BooksService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.getData();
  }

  getSuggestion(): void {
    this.displaySuggestion = !this.displaySuggestion;
    if (this.suggestions.length === 0)
      this.booksService
        .searchSuggestion(this.filename)
        .pipe()
        .subscribe(name => {
          this.suggestions = name;
          this.openDialog();
        });
    else this.openDialog();
  }

  generateURL(): string {
    return BASE_URL_BOOKS + this.filename.split('.')[0];
  }

  getData(): void {
    this.booksService.getDataOnBook(this.filename.substring(0, this.filename.length - 4)).subscribe(value => {
      this.data = value;
      this.title = value.title;
      this.author = value.authors.length === 0 ? 'no author' : value.authors[0].name;
      this.subjects = value.subjects.length > 2 ? value.subjects.slice(0, 2) : value.subjects;
      this.languages = value.languages;
      this.imageSrc = value.formats['image/jpeg'] === undefined ? NO_IMAGE : value.formats['image/jpeg'];
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(SuggestionComponent, {
      width: '90rem',
      data: {
        title: this.title,
        suggestions: this.suggestions
      }
    });

    dialogRef.afterClosed().subscribe(() => {
      console.log('The dialog was closed');
    });
  }
}
