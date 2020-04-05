import { Component, OnInit } from '@angular/core';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { NgForm } from '@angular/forms';

import { BooksService } from '../services/books.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  filesName: string[] | undefined = [];
  suggestions: string[] = [];
  loading = false;

  currentForm: NgForm = new NgForm([], []);

  pageLength = 100;
  pageIndex = 0;
  pageSize = 10;
  pageOptions = [5, 10, 25];
  nbPages = 0;
  pagesTab: number[] = [];
  nbPageToDisplay = 10;

  memoire: Map<number, Map<number, string[]>> = new Map<number, Map<number, string[]>>();
  currentWord = '';
  currentFiltre = '';

  // dueta
  constructor(private accountService: AccountService, private loginModalService: LoginModalService, private booksService: BooksService) {}

  ngOnInit(): void {
    this.pageOptions.forEach(o => this.memoire.set(o, new Map<number, string[]>()));
  }

  onSubmit(searchBooks: NgForm): void {
    this.loading = true;

    if (
      this.currentWord === searchBooks.value.word &&
      this.currentFiltre === searchBooks.value.filtre &&
      this.memoire.get(this.pageSize)?.get(this.pageIndex)
    ) {
      this.filesName = this.memoire.get(this.pageSize)?.get(this.pageIndex);
      this.loading = false;
    } else {
      this.completeSearch(searchBooks);
    }
    this.currentForm = searchBooks;
    this.updatePageTab();
  }

  completeSearch(searchBooks: NgForm): void {
    this.filesName = [];
    this.currentWord = searchBooks.value.word;
    this.booksService
      .searchBooks(searchBooks.value, this.pageIndex, this.pageSize)
      .pipe()
      .subscribe(data => {
        this.filesName = data.page;
        this.pageLength = data.pageLength;
        this.pageIndex = data.pageIndex;
        this.nbPages = data.nbPages;
        if (this.filesName) this.memoire.get(this.pageSize)?.set(this.pageIndex, this.filesName);
        this.loading = false;
      });
  }

  handlePage(e: any): void {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
    this.onSubmit(this.currentForm);
  }

  changePage(p: number): void {
    this.pageIndex = p;
    this.onSubmit(this.currentForm);
  }

  updatePageTab(): void {
    this.pagesTab = [];
    let begin: number;
    let end: number;
    if (this.pageIndex < this.nbPageToDisplay / 2) {
      begin = 0;
      end = this.nbPageToDisplay;
    } else if (this.pageIndex > this.nbPages - 1 - this.nbPageToDisplay) {
      begin = this.nbPages - this.nbPageToDisplay;
      end = this.nbPages;
    } else {
      begin = this.pageIndex - this.nbPageToDisplay / 2;
      end = this.pageIndex + this.nbPageToDisplay / 2;
    }
    for (let i = begin; i < end; i++) this.pagesTab.push(i);
  }
}
