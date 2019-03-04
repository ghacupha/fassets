/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FassetsTestModule } from '../../../test.module';
import { DepreciationDetailComponent } from 'app/entities/depreciation/depreciation-detail.component';
import { Depreciation } from 'app/shared/model/depreciation.model';

describe('Component Tests', () => {
    describe('Depreciation Management Detail Component', () => {
        let comp: DepreciationDetailComponent;
        let fixture: ComponentFixture<DepreciationDetailComponent>;
        const route = ({ data: of({ depreciation: new Depreciation(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FassetsTestModule],
                declarations: [DepreciationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DepreciationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DepreciationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.depreciation).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
