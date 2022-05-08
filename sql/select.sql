# Count number of citizens by postal code

select distinct postal_code,
       count(id) as number_of_citizens
from citizens
group by postal_code;

# Count citizens by postal code and by number of vaccinations (not correct!!!)

select distinct postal_code,
                count(v.id) as number_of_vaccinations_per_citizen,
                count(distinct c.id) as number_of_citizens
from citizens as c
         join vaccinations v on c.id = v.citizen_id
group by postal_code, c.id
order by postal_code;